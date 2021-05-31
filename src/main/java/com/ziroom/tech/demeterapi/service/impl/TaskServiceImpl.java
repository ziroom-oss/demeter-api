package com.ziroom.tech.demeterapi.service.impl;

import com.google.common.collect.Lists;
import com.ziroom.tech.demeterapi.common.EhrComponent;
import com.ziroom.tech.demeterapi.common.OperatorContext;
import com.ziroom.tech.demeterapi.common.PageListResp;
import com.ziroom.tech.demeterapi.common.StorageComponent;
import com.ziroom.tech.demeterapi.common.enums.*;
import com.ziroom.tech.demeterapi.common.exception.BusinessException;
import com.ziroom.tech.demeterapi.dao.entity.*;
import com.ziroom.tech.demeterapi.dao.mapper.*;
import com.ziroom.tech.demeterapi.po.dto.Resp;

import com.ziroom.tech.demeterapi.po.dto.req.task.*;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.EhrUserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.EhrUserResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserResp;
import com.ziroom.tech.demeterapi.po.dto.resp.halo.AuthResp;
import com.ziroom.tech.demeterapi.po.dto.resp.storage.ZiroomFile;
import com.ziroom.tech.demeterapi.po.dto.resp.task.*;
import com.ziroom.tech.demeterapi.service.HaloService;
import com.ziroom.tech.demeterapi.service.MessageService;
import com.ziroom.tech.demeterapi.service.TaskService;
import com.ziroom.tech.sia.hunter.taskstatus.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author daijiankun
 */
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Resource
    private DemeterAssignTaskDao demeterAssignTaskDao;
    @Resource
    private DemeterSkillTaskDao demeterSkillTaskDao;
    @Resource
    private DemeterTaskUserDao demeterTaskUserDao;
    @Resource
    private TaskFinishConditionDao taskFinishConditionDao;
    @Resource
    private TaskFinishConditionInfoDao taskFinishConditionInfoDao;
    @Resource
    private TaskFinishOutcomeDao taskFinishOutcomeDao;
    @Resource
    private EhrComponent ehrComponent;
    @Resource
    private StorageComponent storageComponent;

    @Resource
    private HaloService haloService;

    @Resource
    private MessageService messageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp<Object> createAssignTask(AssignTaskReq assignTaskReq) {
        DemeterAssignTask entity = new DemeterAssignTask();
        BeanUtils.copyProperties(assignTaskReq, entity);
        entity.setTaskStatus(AssignTaskStatus.ONGOING.getCode());
        entity.setPublisher(OperatorContext.getOperator());
        MultipartFile attachment = assignTaskReq.getAttachment();
        if (Objects.nonNull(attachment)) {
            ZiroomFile ziroomFile = storageComponent.uploadFile(attachment);
            entity.setAttachmentUrl(ziroomFile.getUrl());
            entity.setAttachmentName(ziroomFile.getOriginal_filename());
        }
        entity.setCreateId(OperatorContext.getOperator());
        entity.setModifyId(OperatorContext.getOperator());
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        demeterAssignTaskDao.insertSelective(entity);

        // DemeterTaskUser
        // batchInsert?

        List<String> taskReceiver = assignTaskReq.getTaskReceiver();
        this.assignTask(taskReceiver, entity);

        // TaskFinishCondition
        List<String> taskFinishConditionList = assignTaskReq.getTaskFinishCondition();
        if (CollectionUtils.isNotEmpty(taskFinishConditionList)) {
            taskFinishConditionList.forEach(condition -> {
                TaskFinishCondition taskFinishCondition = TaskFinishCondition.builder()
                        .taskId(entity.getId())
                        .taskType(TaskType.ASSIGN.getCode())
                        .taskFinishContent(condition)
                        .createTime(new Date())
                        .modifyTime(new Date())
                        .createId(OperatorContext.getOperator())
                        .modifyId(OperatorContext.getOperator())
                        .build();
                taskFinishConditionDao.insertSelective(taskFinishCondition);
            });
        }

//        messageService.sendAssignTaskCreated(entity.getId(), OperatorContext.getOperator(), taskReceiverString);

        return Resp.success();
    }

    private void assignTask(List<String> taskReceiver, DemeterAssignTask task) {
        if (CollectionUtils.isNotEmpty(taskReceiver)) {
            taskReceiver.forEach(receiver -> {
                // 查询已分派但拒绝的任务
                DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
                demeterTaskUserExample.createCriteria()
                        .andTaskIdEqualTo(task.getId())
                        .andTaskTypeEqualTo(TaskType.ASSIGN.getCode())
                        .andReceiverUidEqualTo(receiver)
                        .andTaskStatusEqualTo(AssignTaskFlowStatus.REJECTED.getCode());
                List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
                if (CollectionUtils.isNotEmpty(demeterTaskUsers)) {
                    DemeterTaskUser demeterTaskUser = demeterTaskUsers.get(0);
                    DemeterTaskUser update = DemeterTaskUser.builder()
                            .id(demeterTaskUser.getId())
                            .taskStatus(AssignTaskFlowStatus.UNCLAIMED.getCode())
                            .build();
                    demeterTaskUserDao.updateByPrimaryKeySelective(update);
                } else {
                    DemeterTaskUser demeterTaskUser = DemeterTaskUser.builder()
                            .taskId(task.getId())
                            .receiverUid(receiver)
                            // 增加记录但为未认领状态
                            .taskStatus(AssignTaskFlowStatus.UNCLAIMED.getCode())
                            .checkResult(task.getNeedAcceptance() == 1 ? CheckoutResult.NEED_CHECKOUT.getCode() : CheckoutResult.NO_CHECKOUT.getCode())
                            .taskType(TaskType.ASSIGN.getCode())
                            .taskEndTime(task.getTaskEndTime())
                            .createId(OperatorContext.getOperator())
                            .modifyId(OperatorContext.getOperator())
                            .createTime(new Date())
                            .modifyTime(new Date())
                            .build();
                    demeterTaskUserDao.insertSelective(demeterTaskUser);
                }
            });
        }
    }

    @Override
    public Resp<AssignDetailResp> getAssignTask(Long id) {
        AssignDetailResp resp = new AssignDetailResp();
        DemeterAssignTask demeterAssignTask = demeterAssignTaskDao.selectByPrimaryKey(id);
        BeanUtils.copyProperties(demeterAssignTask, resp);
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria()
                .andTaskIdEqualTo(id)
                .andTaskTypeEqualTo(TaskType.ASSIGN.getCode());
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        List<String> receiverList = demeterTaskUsers.stream().map(DemeterTaskUser::getReceiverUid).collect(Collectors.toList());
        resp.setTaskReceiver(receiverList);
        TaskFinishConditionExample taskFinishConditionExample = new TaskFinishConditionExample();
        taskFinishConditionExample.createCriteria()
                .andTaskIdEqualTo(id)
                .andTaskTypeEqualTo(TaskType.ASSIGN.getCode());
        List<TaskFinishCondition> taskFinishConditions = taskFinishConditionDao.selectByExample(taskFinishConditionExample);
        resp.setTaskFinishConditionList(taskFinishConditions);
        return Resp.success(resp);
    }

    @Override
    public Resp<SkillDetailResp> getSkillTask(Long id) {
        SkillDetailResp resp = new SkillDetailResp();
        DemeterSkillTask demeterSkillTask = demeterSkillTaskDao.selectByPrimaryKey(id);
        BeanUtils.copyProperties(demeterSkillTask, resp);
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria()
                .andTaskIdEqualTo(id)
                .andTaskTypeEqualTo(TaskType.SKILL.getCode());
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        List<String> receiverList = demeterTaskUsers.stream().map(DemeterTaskUser::getReceiverUid).collect(Collectors.toList());
        resp.setTaskReceiver(receiverList);
        TaskFinishConditionExample taskFinishConditionExample = new TaskFinishConditionExample();
        taskFinishConditionExample.createCriteria()
                .andTaskIdEqualTo(id)
                .andTaskTypeEqualTo(TaskType.SKILL.getCode());
        List<TaskFinishCondition> taskFinishConditions = taskFinishConditionDao.selectByExample(taskFinishConditionExample);
        resp.setTaskFinishConditionList(taskFinishConditions);
        return Resp.success(resp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp<Object> createSkillTask(SkillTaskReq skillTaskReq) {
        DemeterSkillTask entity = new DemeterSkillTask();
        BeanUtils.copyProperties(skillTaskReq, entity);
        entity.setPublisher(OperatorContext.getOperator());
        entity.setCreateId(OperatorContext.getOperator());
        entity.setModifyId(OperatorContext.getOperator());
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setSkillId(233L);
        entity.setSkillLevel(skillTaskReq.getSkillLevel());
        List<String> taskFinishCondition = skillTaskReq.getTaskFinishCondition();
        MultipartFile attachment = skillTaskReq.getAttachment();
        if (Objects.nonNull(attachment)) {
            ZiroomFile ziroomFile = storageComponent.uploadFile(attachment);
            entity.setAttachmentUrl(ziroomFile.getUrl());
            entity.setAttachmentName(ziroomFile.getOriginal_filename());
        }
        demeterSkillTaskDao.insertSelective(entity);
        if (CollectionUtils.isNotEmpty(taskFinishCondition)) {
            taskFinishCondition.forEach(condition -> {
                if (StringUtils.isEmpty(condition)) {
                    throw new BusinessException("任务条件内容不能为空");
                }
                TaskFinishCondition taskFinishEntity = TaskFinishCondition.builder()
                        .modifyId(OperatorContext.getOperator())
                        .createId(OperatorContext.getOperator())
                        .taskFinishContent(condition)
                        .modifyTime(new Date())
                        .createTime(new Date())
                        .taskType(TaskType.SKILL.getCode())
                        .taskId(entity.getId())
                        .build();
                taskFinishConditionDao.insertSelective(taskFinishEntity);
            });
        }
        return Resp.success();
    }

    // todo test
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp<Object> updateAssignTask(AssignTaskReq assignTaskReq) {
        // 校验任务是否被认领
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria()
                .andTaskIdEqualTo(assignTaskReq.getId())
                .andTaskTypeEqualTo(TaskType.ASSIGN.getCode());
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        demeterTaskUsers.forEach(x -> {
            if (TaskType.ASSIGN.getCode().equals(x.getTaskType())) {
                if (!x.getTaskStatus().equals(AssignTaskFlowStatus.UNCLAIMED.getCode())) {
                    throw new BusinessException("该指派任务已被认领，不可编辑");
                }
            } else if (TaskType.SKILL.getCode().equals(x.getTaskType())) {
//                if (!x.getTaskStatus().equals(SkillTaskFlowStatus.UNFORBIDDEN.getCode()) ||
//                        !x.getTaskStatus().equals(SkillTaskFlowStatus.FORBIDDEN.getCode())) {
//                }
                // DemeterTaskUser表中出现技能类任务表示任务已被认领
                throw new BusinessException("该技能任务已被认领，不可编辑");
            }
        });

        DemeterAssignTask entity = new DemeterAssignTask();
        BeanUtils.copyProperties(assignTaskReq, entity);
        entity.setModifyId(OperatorContext.getOperator());
        entity.setUpdateTime(new Date());

        MultipartFile attachment = assignTaskReq.getAttachment();
        if (Objects.nonNull(attachment)) {
            ZiroomFile ziroomFile = storageComponent.uploadFile(attachment);
            entity.setAttachmentUrl(ziroomFile.getUrl());
            entity.setAttachmentName(ziroomFile.getOriginal_filename());
        }

        demeterAssignTaskDao.updateByPrimaryKeySelective(entity);
        try {
            updateTaskFinishCondition(assignTaskReq.getTaskFinishCondition(), assignTaskReq.getId(), TaskType.ASSIGN.getCode());
        } catch (BusinessException exception) {
            log.info("update task finish condition occur exception = {}", exception.getMessage());
            return Resp.error(exception.getMessage());
        }

        try {
            updateTaskUser(assignTaskReq);
        } catch (BusinessException exception) {
            log.info("update task user occur exception = {}", exception.getMessage());
            return Resp.error(exception.getMessage());
        }
        return Resp.success();
    }

    @Override
    public Resp<Object> updateAssignTaskStatus(Long taskId, Integer taskType, Integer taskStatus) {
        if (taskType.equals(TaskType.SKILL.getCode())) {
            DemeterSkillTask demeterSkillTask = demeterSkillTaskDao.selectByPrimaryKey(taskId);
            if (Objects.isNull(demeterSkillTask)) {
                throw new BusinessException("任务不存在！taskId = " + taskId);
            }
            switch (SkillTaskStatus.getByCode(taskStatus)) {
                case UNFORBIDDEN:
                    if (demeterSkillTask.getTaskStatus().equals(SkillTaskStatus.UNFORBIDDEN.getCode())) {
                        return Resp.error("此任务已经为启用状态，请勿重复操作！");
                    }
                    DemeterSkillTask update = new DemeterSkillTask();
                    update.setId(taskId);
                    update.setTaskStatus(SkillTaskStatus.UNFORBIDDEN.getCode());
                    demeterSkillTaskDao.updateByPrimaryKeySelective(update);
                    break;
                case FORBIDDEN:
                    // 技能类任务可随时禁用
                    if (demeterSkillTask.getTaskStatus().equals(SkillTaskStatus.FORBIDDEN.getCode())) {
                        return Resp.error("此任务已经为启用状态，请勿重复操作！");
                    }
                    DemeterSkillTask updateForbidden = new DemeterSkillTask();
                    updateForbidden.setId(taskId);
                    updateForbidden.setTaskStatus(SkillTaskStatus.FORBIDDEN.getCode());
                    demeterSkillTaskDao.updateByPrimaryKeySelective(updateForbidden);
                    break;
                default:
            }
            return Resp.success();
        } else if (taskType.equals(TaskType.ASSIGN.getCode())) {
            DemeterAssignTask demeterAssignTask = demeterAssignTaskDao.selectByPrimaryKey(taskId);
            if (taskStatus.equals(AssignTaskStatus.ONGOING.getCode())) {
                if (Objects.isNull(demeterAssignTask)) {
                    return Resp.error("任务不存在！taskId = " + taskId);
                }
                if (demeterAssignTask.getTaskStatus().equals(AssignTaskStatus.ONGOING.getCode())) {
                    return Resp.error("此任务已经为开启状态，请勿重复开启！");
                }
                DemeterAssignTask update = new DemeterAssignTask();
                update.setId(taskId);
                update.setTaskStatus(AssignTaskStatus.ONGOING.getCode());
                demeterAssignTaskDao.updateByPrimaryKeySelective(update);
            } else if (taskStatus.equals(AssignTaskStatus.CLOSED.getCode())) {
                if (demeterAssignTask.getTaskStatus().equals(AssignTaskStatus.CLOSED.getCode())) {
                    return Resp.error("此任务已经为关闭状态，请勿重复关闭！");
                }
                // 待认领、进行中，待验收 任务不可关闭
                List<Integer> forbiddenList = new ArrayList<>();
                forbiddenList.add(AssignTaskFlowStatus.UNCLAIMED.getCode());
                forbiddenList.add(AssignTaskFlowStatus.ONGOING.getCode());
                forbiddenList.add(AssignTaskFlowStatus.WAIT_ACCEPTANCE.getCode());
                DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
                demeterTaskUserExample.createCriteria()
                        .andTaskIdEqualTo(taskId)
                        .andTaskTypeEqualTo(TaskType.ASSIGN.getCode());
                List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
                List<Integer> existStatusList = demeterTaskUsers.stream().map(DemeterTaskUser::getTaskStatus).collect(Collectors.toList());
                List<Integer> intersection = forbiddenList.stream().filter(existStatusList::contains).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(intersection)) {
                    return Resp.error("存在待认领、进行中或待验收状态的接收者，无法关闭当前任务：" + demeterAssignTask.getTaskName());
                }
                DemeterAssignTask update = new DemeterAssignTask();
                update.setId(taskId);
                update.setTaskStatus(AssignTaskStatus.CLOSED.getCode());
                demeterAssignTaskDao.updateByPrimaryKeySelective(update);
            }
            return Resp.success();
        }
        return Resp.error("未知的任务类型");
    }

    // todo test
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp<Object> updateSkillTask(SkillTaskReq skillTaskReq) {
        DemeterSkillTask entity = new DemeterSkillTask();
        BeanUtils.copyProperties(skillTaskReq, entity);
        entity.setModifyId(OperatorContext.getOperator());
        entity.setUpdateTime(new Date());
        demeterSkillTaskDao.updateByPrimaryKeySelective(entity);
        try {
            updateTaskFinishCondition(skillTaskReq.getTaskFinishCondition(), skillTaskReq.getId(), TaskType.SKILL.getCode());
        } catch (BusinessException exception) {
            log.info("update task finish condition occur exception = {}", exception.getMessage());
            return Resp.error(exception.getMessage());
        }
        return Resp.success();
    }

    /**
     * 更新任务认领关系
     * @param assignTaskReq 指派类任务请求体
     */
    private void updateTaskUser(AssignTaskReq assignTaskReq) {
        List<String> newReceiverList = assignTaskReq.getTaskReceiver();
        Long taskId = assignTaskReq.getId();
        if (CollectionUtils.isNotEmpty(newReceiverList)) {
            // 原有待认领关系全部删除
            DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
            demeterTaskUserExample.createCriteria()
                    .andTaskIdEqualTo(taskId)
                    .andTaskTypeEqualTo(TaskType.ASSIGN.getCode());
            List<DemeterTaskUser> toBeDeleteDemeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
            toBeDeleteDemeterTaskUsers.forEach(demeterTaskUser -> demeterTaskUserDao.deleteByPrimaryKey(demeterTaskUser.getId()));
            newReceiverList.forEach(receiver -> {
                DemeterTaskUser demeterTaskUser = DemeterTaskUser.builder()
                        .taskId(taskId)
                        .receiverUid(receiver)
                        .checkResult(assignTaskReq.getNeedAcceptance() == 1 ? CheckoutResult.NEED_CHECKOUT.getCode() : CheckoutResult.NO_CHECKOUT.getCode())
                        // 增加记录但为未认领状态
                        .taskStatus(AssignTaskFlowStatus.UNCLAIMED.getCode())
                        .taskType(TaskType.ASSIGN.getCode())
                        .taskEndTime(assignTaskReq.getTaskEndTime())
                        .createId(OperatorContext.getOperator())
                        .modifyId(OperatorContext.getOperator())
                        .createTime(new Date())
                        .modifyTime(new Date())
                        .build();
                demeterTaskUserDao.insertSelective(demeterTaskUser);
            });
        }
    }

    private void updateTaskFinishCondition(List<String> newCondition, Long taskId, Integer taskType) {
        if (CollectionUtils.isNotEmpty(newCondition)) {
            // 原有任务完成条件全部删除
            TaskFinishConditionExample taskFinishConditionExample = new TaskFinishConditionExample();
            taskFinishConditionExample.createCriteria()
                    .andTaskIdEqualTo(taskId)
                    .andTaskTypeEqualTo(taskType);
            List<TaskFinishCondition> toBeDeletedTaskCondition = taskFinishConditionDao.selectByExample(taskFinishConditionExample);
            toBeDeletedTaskCondition.forEach(condition -> taskFinishConditionDao.deleteByPrimaryKey(condition.getId()));
            // insert修改后任务完成条件
            newCondition.forEach(condition -> {
                TaskFinishCondition entity = TaskFinishCondition.builder()
                        .taskId(taskId)
                        .taskType(taskType)
                        .taskFinishContent(condition)
                        .createTime(new Date())
                        .modifyTime(new Date())
                        .createId(OperatorContext.getOperator())
                        .modifyId(OperatorContext.getOperator())
                        .build();
                taskFinishConditionDao.insertSelective(entity);
            });
        }
    }

    @Override
    public PageListResp<ReleaseQueryResp> getReleaseList(TaskListQueryReq taskListQueryReq) {
        PageListResp<ReleaseQueryResp> pageListResp = new PageListResp<>();

        List<ReleaseQueryResp> respList = new ArrayList<>(16);

        DemeterSkillTaskExample skillTaskExample = new DemeterSkillTaskExample();
        DemeterSkillTaskExample.Criteria skillTaskExampleCriteria = skillTaskExample.createCriteria();
        DemeterAssignTaskExample assignTaskExample = new DemeterAssignTaskExample();
        DemeterAssignTaskExample.Criteria assignTaskExampleCriteria = assignTaskExample.createCriteria();

        // 员工只能看到本人创建或接收的任务，部门管理者可以看到本部门员工创建或接收的所有任务，超级管理员可以看到所有部门员工创建或接收的所有任务。

        CurrentRole currentRole = this.getCurrentRole();
        String publisher = taskListQueryReq.getSystemCode();
        switch (currentRole) {
            case SUPER:
                // 查询所有记录
                if (StringUtils.isNotEmpty(publisher)) {
                    assignTaskExampleCriteria.andPublisherEqualTo(publisher);
                    skillTaskExampleCriteria.andPublisherEqualTo(publisher);
                }
                break;
            case DEPT:

                List<String> currentDeptUsers = this.getCurrentDeptUsers();
                if (StringUtils.isNotEmpty(publisher)) {
                    if (currentDeptUsers.contains(publisher)) {
                        assignTaskExampleCriteria.andPublisherEqualTo(publisher);
                        skillTaskExampleCriteria.andPublisherEqualTo(publisher);
                    } else {
                        return PageListResp.emptyList();
                    }
                } else {
                    assignTaskExampleCriteria.andPublisherIn(currentDeptUsers);
                    skillTaskExampleCriteria.andPublisherIn(currentDeptUsers);
                }
                break;
            case PLAIN:
                if (StringUtils.isNotEmpty(publisher) && !publisher.equals(OperatorContext.getOperator())) {
                    return PageListResp.emptyList();
                }
                assignTaskExampleCriteria.andPublisherEqualTo(OperatorContext.getOperator());
                skillTaskExampleCriteria.andPublisherEqualTo(OperatorContext.getOperator());
            default:
        }

//        List<DemeterTaskUser> demeterTaskUsers;
//        if (StringUtils.isNotEmpty(publisher)) {
//            // 查询任务接收表 List<Long> taskIdList = queryTaskReceive();
//            DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
//            demeterTaskUserExample.createCriteria().andReceiverUidEqualTo(publisher);
//            demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
//            if (CollectionUtils.isNotEmpty(demeterTaskUsers)) {
//                List<Long> taskIdList = demeterTaskUsers.stream().map(DemeterTaskUser::getTaskId).collect(Collectors.toList());
//                assignTaskExampleCriteria.andIdIn(taskIdList);
//                skillTaskExampleCriteria.andIdIn(taskIdList);
//            } else {
//                return PageListResp.emptyList();
//            }
//        }

        List<DemeterSkillTask> skillTasks = new ArrayList<>(16);
        List<DemeterAssignTask> assignTasks = new ArrayList<>(16);

        String nameOrNo = taskListQueryReq.getNameOrNo();
        if (StringUtils.isNotEmpty(nameOrNo)) {
            if (nameOrNo.startsWith(TaskIdPrefix.ASSIGN_PREFIX.getDesc())) {
                assignTaskExampleCriteria.andIdEqualTo(Long.parseLong(nameOrNo.split("-")[1]));
            } else if (nameOrNo.startsWith(TaskIdPrefix.SKILL_PREFIX.getDesc())) {
                skillTaskExampleCriteria.andIdEqualTo(Long.parseLong(nameOrNo.split("-")[1]));
            } else {
                assignTaskExampleCriteria.andTaskNameLike(nameOrNo);
                skillTaskExampleCriteria.andTaskNameLike(nameOrNo);
            }
        }

        Long skillTreeId = taskListQueryReq.getSkillTreeId();
        if (Objects.nonNull(skillTreeId)) {
            skillTaskExampleCriteria.andSkillIdEqualTo(skillTreeId);
        }

        Integer skillPointLevel = taskListQueryReq.getSkillPointLevel();
        if (Objects.nonNull(skillPointLevel)) {
            if (SkillPointLevel.ALL.getCode().equals(skillPointLevel)) {
                List<Integer> allLevels = SkillPointLevel.getAllSkillLevel().stream()
                        .map(SkillPointLevel::getCode).collect(Collectors.toList());
                skillTaskExampleCriteria.andSkillLevelIn(allLevels);
            } else {
                skillTaskExampleCriteria.andSkillLevelEqualTo(skillPointLevel);
            }
        }

        if (Objects.nonNull(taskListQueryReq.getTaskType())) {
            if (taskListQueryReq.getTaskType().equals(TaskType.SKILL.getCode())) {
                if (Objects.nonNull(taskListQueryReq.getTaskStatus())) {
                    if (taskListQueryReq.getTaskStatus().equals(SkillTaskStatus.ALL.getCode())) {
                        skillTaskExampleCriteria.andTaskStatusIn(SkillTaskStatus.getAllTaskType().stream()
                                .map(SkillTaskStatus::getCode).collect(Collectors.toList()));
                    } else {
                        skillTaskExampleCriteria.andTaskStatusEqualTo(taskListQueryReq.getTaskStatus());
                    }
                }
                skillTasks = demeterSkillTaskDao.selectByExample(skillTaskExample);
            } else if (taskListQueryReq.getTaskType().equals(TaskType.ASSIGN.getCode())) {
                if (Objects.nonNull(taskListQueryReq.getTaskStatus())) {
                    if (taskListQueryReq.getTaskStatus().equals(AssignTaskStatus.ALL.getCode())) {
                        skillTaskExampleCriteria.andTaskStatusIn(AssignTaskStatus.getAllTaskType().stream()
                                .map(AssignTaskStatus::getCode).collect(Collectors.toList()));
                    } else {
                        assignTaskExampleCriteria.andTaskStatusEqualTo(taskListQueryReq.getTaskStatus());
                    }
                }
                assignTasks = demeterAssignTaskDao.selectByExample(assignTaskExample);
            } else if (taskListQueryReq.getTaskType().equals(TaskType.ALL.getCode())){
                skillTasks = demeterSkillTaskDao.selectByExample(skillTaskExample);
                assignTasks = demeterAssignTaskDao.selectByExample(assignTaskExample);
            }
        }

        if (CollectionUtils.isNotEmpty(skillTasks)) {
            Set<String> publisherSet = skillTasks.stream()
                    .map(DemeterSkillTask::getPublisher).collect(Collectors.toSet());
            Set<UserResp> userDetail = ehrComponent.getUserDetail(publisherSet);
            Map<String, UserResp> userRespMap = userDetail.stream().collect(Collectors.toMap(UserResp::getCode, (Function.identity())));
            skillTasks.forEach(task -> {
                List<String> receiverList = this.getReceiverListFromSkillPointId(task.getId());
                ReleaseQueryResp releaseQueryResp = ReleaseQueryResp.builder()
                        .id(task.getId())
                        .taskNo(TaskIdPrefix.SKILL_PREFIX.getDesc() + task.getId())
                        .taskName(task.getTaskName())
                        .taskReceiverName(String.join(",", receiverList))
                        .taskStatus(task.getTaskStatus())
                        .taskStatusName(SkillTaskStatus.getByCode(task.getTaskStatus()).getDesc())
                        .taskType(TaskType.SKILL.getCode())
                        .taskTypeName(TaskType.SKILL.getDesc())
                        .taskCreateTime(task.getCreateTime())
                        .skillLevel(task.getSkillLevel())
                        .skillLevelName(SkillPointLevel.getByCode(task.getSkillLevel()).getDesc())
                        .skillTreeId(task.getSkillId())
                        // TODO: 2021/5/27  
                        .skillTreeName("技能树待做")
                        .growthValue(task.getSkillReward())
                        .publisher(task.getPublisher())
                        .publisherName(userRespMap.get(task.getPublisher()).getName())
                        .build();
                respList.add(releaseQueryResp);
            });
        }

        if (CollectionUtils.isNotEmpty(assignTasks)) {
            Set<String> publisherSet = assignTasks.stream().map(DemeterAssignTask::getPublisher).collect(Collectors.toSet());
            Set<UserResp> userDetail = ehrComponent.getUserDetail(publisherSet);
            Map<String, UserResp> userRespMap = userDetail.stream().collect(Collectors.toMap(UserResp::getCode, (Function.identity())));
            assignTasks.forEach(task -> {
                List<String> receiverNameList = getReceiverListFromTaskUserEntity(task);
                ReleaseQueryResp releaseQueryResp = ReleaseQueryResp.builder()
                        .id(task.getId())
                        .taskNo(TaskIdPrefix.ASSIGN_PREFIX.getDesc() + task.getId())
                        .taskName(task.getTaskName())
                        .taskReceiverName(String.join(",", receiverNameList))
                        .taskStatus(task.getTaskStatus())
                        .taskStatusName(AssignTaskStatus.getByCode(task.getTaskStatus()).getDesc())
                        .taskType(TaskType.ASSIGN.getCode())
                        .taskTypeName(TaskType.ASSIGN.getDesc())
                        .taskCreateTime(task.getCreateTime())
                        .growthValue(task.getTaskReward())
                        .publisher(task.getPublisher())
                        .publisherName(userRespMap.get(task.getPublisher()).getName())
                        .build();
                respList.add(releaseQueryResp);
            });
        }

        respList.sort(Comparator.comparing(ReleaseQueryResp::getTaskCreateTime).reversed());
        pageListResp.setTotal(respList.size());
        List<ReleaseQueryResp> rtv = respList.stream().skip(taskListQueryReq.getStart()).limit(taskListQueryReq.getPageSize()).collect(Collectors.toList());
        pageListResp.setData(rtv);
        return pageListResp;
    }

    @Override
    public PageListResp<ReceiveQueryResp> getExecuteList(TaskListQueryReq taskListQueryReq) {
        PageListResp<ReceiveQueryResp> pageListResp = new PageListResp<>();
        // 员工只能看到本人接收的任务，部门管理者可以看到本部门员工接收的所有任务，超级管理员可以看到所有部门员工接收的所有任务。
        List<ReceiveQueryResp> respList = new ArrayList<>(16);

        DemeterSkillTaskExample skillTaskExample = new DemeterSkillTaskExample();
        DemeterSkillTaskExample.Criteria skillTaskExampleCriteria = skillTaskExample.createCriteria();
        DemeterAssignTaskExample assignTaskExample = new DemeterAssignTaskExample();
        DemeterAssignTaskExample.Criteria assignTaskExampleCriteria = assignTaskExample.createCriteria();

        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        DemeterTaskUserExample.Criteria demeterTaskUserCriteria = demeterTaskUserExample.createCriteria();

        CurrentRole currentRole = this.getCurrentRole();
        String receiverCode = taskListQueryReq.getSystemCode();
        switch (currentRole) {
            case SUPER:
                // 全部
                if (StringUtils.isNotEmpty(receiverCode)) {
                    demeterTaskUserCriteria.andReceiverUidEqualTo(receiverCode);
                }
                break;
            case DEPT:
                if (StringUtils.isNotEmpty(receiverCode)) {
                    demeterTaskUserCriteria.andReceiverUidEqualTo(receiverCode);
                } else {
                    List<String> currentDeptUsers = this.getCurrentDeptUsers();
                    demeterTaskUserCriteria.andReceiverUidIn(currentDeptUsers);
                }
                break;
            case PLAIN:
                demeterTaskUserCriteria.andReceiverUidEqualTo(OperatorContext.getOperator());
            default:
        }
        Integer taskType = taskListQueryReq.getTaskType();
        Integer taskStatus = taskListQueryReq.getTaskStatus();
        if (taskType != null && TaskType.isValid(taskType)) {
            switch (TaskType.getByCode(taskType)) {
                case ALL:
                    break;
                case SKILL:
                    demeterTaskUserCriteria.andTaskTypeEqualTo(TaskType.SKILL.getCode());
                    if (taskStatus != null && SkillTaskFlowStatus.isValid(taskStatus)) {
                        if (taskStatus.equals(SkillTaskFlowStatus.ALL.getCode())) {
                            demeterTaskUserCriteria.andTaskStatusIn(SkillTaskStatus.getAllTaskType().stream()
                                    .map(SkillTaskStatus::getCode).collect(Collectors.toList()));
                        } else {
                            demeterTaskUserCriteria.andTaskStatusEqualTo(taskStatus);
                        }
                    }
                    break;
                case ASSIGN:
                    demeterTaskUserCriteria.andTaskTypeEqualTo(TaskType.ASSIGN.getCode());
                    if (taskStatus != null && AssignTaskFlowStatus.isValid(taskStatus)) {
                        if (taskStatus.equals(AssignTaskStatus.ALL.getCode())) {
                            demeterTaskUserCriteria.andTaskStatusIn(AssignTaskStatus.getAllTaskType().stream()
                                    .map(AssignTaskStatus::getCode).collect(Collectors.toList()));
                        } else {
                            demeterTaskUserCriteria.andTaskStatusEqualTo(taskStatus);
                        }
                    }
                default:
            }
        }

        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);

        List<Long> skillIds = demeterTaskUsers.stream().filter(u -> u.getTaskType().equals(TaskType.SKILL.getCode())).map(DemeterTaskUser::getTaskId).collect(Collectors.toList());
        List<Long> assignIds = demeterTaskUsers.stream().filter(u -> u.getTaskType().equals(TaskType.ASSIGN.getCode())).map(DemeterTaskUser::getTaskId).collect(Collectors.toList());

        Set<String> receiverId = demeterTaskUsers.stream().map(DemeterTaskUser::getReceiverUid).collect(Collectors.toSet());

        List<DemeterSkillTask> demeterSkillTasks = new ArrayList<>();
        List<DemeterAssignTask> demeterAssignTasks = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(skillIds)) {
            skillTaskExampleCriteria.andIdIn(skillIds);
            demeterSkillTasks = demeterSkillTaskDao.selectByExample(skillTaskExample);
        }
        if (CollectionUtils.isNotEmpty(assignIds)) {
            assignTaskExampleCriteria.andIdIn(assignIds);
            demeterAssignTasks = demeterAssignTaskDao.selectByExample(assignTaskExample);
        }
        // todo 全部查出，效率低
        Set<String> skillPublisher = demeterSkillTasks.stream().map(DemeterSkillTask::getPublisher).collect(Collectors.toSet());
        Set<String> assignPublisher = demeterAssignTasks.stream().map(DemeterAssignTask::getPublisher).collect(Collectors.toSet());
        receiverId.addAll(skillPublisher);
        receiverId.addAll(assignPublisher);

        Set<UserResp> userDetail = ehrComponent.getUserDetail(receiverId);
        Map<String, UserResp> userMap = userDetail.stream().collect(Collectors.toMap(UserResp::getCode, Function.identity()));

        Map<Long, DemeterSkillTask> skillTaskMap = demeterSkillTasks.stream().collect(Collectors.toMap(DemeterSkillTask::getId, Function.identity()));
        Map<Long, DemeterAssignTask> assignTaskMap = demeterAssignTasks.stream().collect(Collectors.toMap(DemeterAssignTask::getId, Function.identity()));

        demeterTaskUsers.forEach(taskUser -> {
            ReceiveQueryResp resp = new ReceiveQueryResp();
            Long taskId = taskUser.getTaskId();

            switch (TaskType.getByCode(taskUser.getTaskType())) {
                case SKILL:
                    DemeterSkillTask skill = skillTaskMap.get(taskId);
                    BeanUtils.copyProperties(skill, resp);
                    resp.setTaskNo(TaskIdPrefix.SKILL_PREFIX.getDesc() + skill.getId());
                    resp.setTaskType(TaskType.SKILL.getCode());
                    resp.setTaskTypeName(TaskType.SKILL.getDesc());
                    resp.setTaskReward(skill.getSkillReward());
                    resp.setReceiver(taskUser.getReceiverUid());
                    resp.setReceiverName(userMap.get(taskUser.getReceiverUid()).getName());
                    resp.setPublisherName(userMap.get(skill.getPublisher()).getName());
                    resp.setTaskFlowStatus(taskUser.getTaskStatus());
                    resp.setTaskFlowStatusName(SkillTaskFlowStatus.getByCode(taskUser.getTaskStatus()).getDesc());
                    respList.add(resp);
                    break;
                case ASSIGN:
                    DemeterAssignTask assign = assignTaskMap.get(taskId);
                    BeanUtils.copyProperties(assign, resp);
                    resp.setTaskNo(TaskIdPrefix.ASSIGN_PREFIX.getDesc() + assign.getId());
                    resp.setTaskType(TaskType.ASSIGN.getCode());
                    resp.setTaskTypeName(TaskType.ASSIGN.getDesc());
                    resp.setReceiver(taskUser.getReceiverUid());
                    resp.setNeedAcceptance(assign.getNeedAcceptance());
                    resp.setReceiverName(userMap.get(taskUser.getReceiverUid()).getName());
                    resp.setPublisherName(userMap.get(assign.getPublisher()).getName());
                    resp.setTaskFlowStatus(taskUser.getTaskStatus());
                    resp.setTaskFlowStatusName(AssignTaskFlowStatus.getByCode(taskUser.getTaskStatus()).getDesc());
                    respList.add(resp);
                default:
            }
        });
        respList.sort(Comparator.comparing(ReceiveQueryResp::getCreateTime).reversed());
        pageListResp.setTotal(respList.size());
        List<ReceiveQueryResp> rtv = respList.stream().skip(taskListQueryReq.getStart()).limit(taskListQueryReq.getPageSize()).collect(Collectors.toList());
        pageListResp.setData(rtv);
        return pageListResp;
    }

    private CurrentRole getCurrentRole() {
        AuthResp permission = haloService.getAuth();
        List<String> roles = permission.getRoles();
        if (roles.contains(CurrentRole.SUPER.getCode())) {
            return CurrentRole.SUPER;
        } else if (roles.contains(CurrentRole.DEPT.getCode())) {
            return CurrentRole.DEPT;
        } else {
            return CurrentRole.PLAIN;
        }
    }

    private List<String> getCurrentDeptUsers() {
        List<EhrUserDetailResp> ehrUserDetail = ehrComponent.getEhrUserDetail(OperatorContext.getOperator());
        EhrUserDetailResp ehrUserDetailResp;
        if (CollectionUtils.isNotEmpty(ehrUserDetail)) {
            ehrUserDetailResp = ehrUserDetail.get(0);
        } else {
            throw new BusinessException("ehr查询用户失败，uid=" + OperatorContext.getOperator());
        }
        Set<EhrUserResp> users = ehrComponent.getUsers(ehrUserDetailResp.getDeptCode(), null);
        return users.stream().map(EhrUserResp::getUserCode).collect(Collectors.toList());
    }

    @Override
    public Resp<AssignDetailResp> getAssignDetail(Long id) {
        return null;
    }

    private List<String> getReceiverListFromSkillPointId(Long skillId) {
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria()
                .andTaskTypeEqualTo(TaskType.SKILL.getCode())
                .andTaskIdEqualTo(skillId);
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        return demeterTaskUsers.stream().map(DemeterTaskUser::getReceiverUid).collect(Collectors.toList());
    }

    private List<String> getReceiverListFromTaskUserEntity(DemeterAssignTask entity) {
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria()
                .andTaskTypeEqualTo(TaskType.ASSIGN.getCode())
                .andTaskIdEqualTo(entity.getId())
                .andTaskStatusNotEqualTo(AssignTaskFlowStatus.UNCLAIMED.getCode());
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        Set<String> uidSet = demeterTaskUsers.stream().map(DemeterTaskUser::getReceiverUid).collect(Collectors.toSet());
        List<String> receiverNameList = Lists.newLinkedList();
        if (CollectionUtils.isNotEmpty(uidSet)) {
            Set<UserResp> userDetail = ehrComponent.getUserDetail(uidSet);
            if (CollectionUtils.isNotEmpty(userDetail)) {
                receiverNameList = userDetail.stream().map(UserResp::getName).collect(Collectors.toList());
            }
        }
        return receiverNameList;
    }

    private String getNameFromUid(String id) {
        UserDetailResp userDetail = ehrComponent.getUserDetail(id);
        if (Objects.nonNull(userDetail)) {
            return userDetail.getUserName();
        } else {
            return "";
        }
    }

    @Override
    public Resp<Object> getSkillDetail(Long id) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp<Object> delete(Long id, Integer taskType) {
        if (taskType.equals(TaskType.SKILL.getCode())) {
            demeterSkillTaskDao.deleteByPrimaryKey(id);
        } else if (taskType.equals(TaskType.ASSIGN.getCode())) {
            demeterAssignTaskDao.deleteByPrimaryKey(id);
        }
        return Resp.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp<Object> acceptTask(Long id, Integer type) {
        if (TaskType.ASSIGN.getCode().equals(type)) {
            DemeterAssignTask demeterAssignTask = demeterAssignTaskDao.selectByPrimaryKey(id);
            if (Objects.isNull(demeterAssignTask)) {
                return Resp.error();
            }
            this.acceptAssignTask(id);
            this.createTaskOutcome(id, type);
        } else if (TaskType.SKILL.getCode().equals(type)) {
            checkSkillForbidden(id);
            DemeterSkillTask demeterSkillTask = demeterSkillTaskDao.selectByPrimaryKey(id);
            if (Objects.isNull(demeterSkillTask)) {
                return Resp.error();
            }
            try {
                this.acceptSkillTask(id);
            } catch (BusinessException exception) {
                log.error(exception.getMessage());
                return Resp.error(exception.getMessage());
            }
            // 技能类任务必须认证
            this.createTaskOutcome(id, type);
        }

        // 任务完成条件
        // TaskFinishConditionInfo
        TaskFinishConditionExample taskFinishConditionExample = new TaskFinishConditionExample();
        taskFinishConditionExample.createCriteria()
                .andTaskIdEqualTo(id)
                .andTaskTypeEqualTo(type);
        List<TaskFinishCondition> taskFinishConditions = taskFinishConditionDao.selectByExample(taskFinishConditionExample);
        if (CollectionUtils.isNotEmpty(taskFinishConditions)) {
            taskFinishConditions.forEach(condition -> {
                TaskFinishConditionInfo info = TaskFinishConditionInfo.builder()
                        .taskId(id)
                        .taskType(type)
                        .taskConditionStatus(TaskConditionStatus.UNFINISHED.getCode())
                        .taskFinishConditionId(condition.getId())
                        .createId(OperatorContext.getOperator())
                        .modifyId(OperatorContext.getOperator())
                        .createTime(new Date())
                        .modifyTime(new Date())
                        .uid(OperatorContext.getOperator())
                        .build();
                taskFinishConditionInfoDao.insertSelective(info);
            });
        }

        messageService.acceptTaskNotice(id, type, OperatorContext.getOperator());
        return Resp.success();
    }

    private void createTaskOutcome(Long id, Integer type) {
        TaskFinishOutcome taskFinishOutcome = TaskFinishOutcome.builder()
                .createId(OperatorContext.getOperator())
                .createTime(new Date())
                .taskId(id)
//                .fileAddress("https://www.ziroom.com")
//                .fileName("文件上传功能待做.jpg")
                .taskType(TaskType.getByCode(type).getCode())
                .modifyId(OperatorContext.getOperator())
                .receiverUid(OperatorContext.getOperator())
                .modifyTime(new Date())
                .build();
        taskFinishOutcomeDao.insertSelective(taskFinishOutcome);
    }

    private void acceptAssignTask(Long id) {
        // 更新DemeterTaskUser 指派类状态
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria()
                .andTaskIdEqualTo(id)
                .andReceiverUidEqualTo(OperatorContext.getOperator())
                .andTaskStatusNotEqualTo(AssignTaskFlowStatus.REJECTED.getCode());
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        if (CollectionUtils.isNotEmpty(demeterTaskUsers) && demeterTaskUsers.size() == 1) {
            // 一个任务对应某个人只有一条未拒绝的记录
            DemeterTaskUser demeterTaskUser = demeterTaskUsers.get(0);
            Integer taskType = demeterTaskUser.getTaskType();
            if (TaskType.ASSIGN.getCode().equals(taskType)) {
                demeterTaskUser.setTaskStatus(AssignTaskFlowStatus.ONGOING.getCode());
            } else if (TaskType.SKILL.getCode().equals(taskType)) {

            }
            demeterTaskUser.setModifyId(OperatorContext.getOperator());
            demeterTaskUser.setModifyTime(new Date());
            demeterTaskUserDao.updateByPrimaryKeySelective(demeterTaskUser);
        } else {
            log.error("重复认领任务 = {}", demeterTaskUsers);
            throw new BusinessException("task assign duplicated");
        }
    }

    private void acceptSkillTask(Long id) {
        // 技能类任务无法指派，所以如果DemeterTaskUser有了任务分配记录，说明该任务已被接收。
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria()
                .andTaskIdEqualTo(id)
                .andTaskTypeEqualTo(TaskType.SKILL.getCode())
                .andReceiverUidEqualTo(OperatorContext.getOperator());
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        if (CollectionUtils.isNotEmpty(demeterTaskUsers)) {
            throw new BusinessException("重复接收技能类任务");
        }
        DemeterTaskUser entity = DemeterTaskUser.builder()
                .modifyTime(new Date())
                .createTime(new Date())
                .modifyId(OperatorContext.getOperator())
                .taskStatus(SkillTaskFlowStatus.ONGOING.getCode())
                .checkResult(CheckoutResult.NEED_CHECKOUT.getCode())
                .createId(OperatorContext.getOperator())
                .taskType(TaskType.SKILL.getCode())
                .receiverUid(OperatorContext.getOperator())
                .taskId(id)
                .build();
        demeterTaskUserDao.insertSelective(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp<Object> rejectTask(RejectTaskReq rejectTaskReq) {
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria()
                .andReceiverUidEqualTo(OperatorContext.getOperator())
                .andTaskIdEqualTo(rejectTaskReq.getTaskId());
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        if (CollectionUtils.isEmpty(demeterTaskUsers)) {
            return Resp.error();
        }
        DemeterTaskUser demeterTaskUser = demeterTaskUsers.get(0);
        // 更新DemeterTaskUser任务状态为拒绝
        DemeterTaskUser entity = DemeterTaskUser.builder()
                .id(demeterTaskUser.getId())
                .taskStatus(AssignTaskFlowStatus.REJECTED.getCode())
                .rejectReason(rejectTaskReq.getReason())
                .modifyTime(new Date())
                .modifyId(OperatorContext.getOperator())
                .build();
        demeterTaskUserDao.updateByPrimaryKeySelective(entity);
        // 拒绝原因处理
        return Resp.success();
    }

    /**
     * 查询任务接收人清单
     * @param taskId id
     * @param taskType type
     * @return List<ReceiverListResp>
     */
    private List<ReceiverListResp> getTaskCheckList(Long taskId, Integer taskType) {
        List<ReceiverListResp> respList = new ArrayList<>(16);
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        DemeterTaskUserExample.Criteria criteria = demeterTaskUserExample.createCriteria();
        criteria.andTaskIdEqualTo(taskId);
        criteria.andTaskTypeEqualTo(taskType);
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        if (CollectionUtils.isNotEmpty(demeterTaskUsers)) {
            demeterTaskUsers.forEach(x -> {
                ReceiverListResp receiverListResp = new ReceiverListResp();
                BeanUtils.copyProperties(x, receiverListResp);
                UserDetailResp userDetail = ehrComponent.getUserDetail(x.getReceiverUid());
                if (Objects.nonNull(userDetail)) {
                    receiverListResp.setReceiverName(userDetail.getUserName());
                    receiverListResp.setReceiverDept(userDetail.getDept());
                }
                switch (TaskType.getByCode(taskType)) {
                    case SKILL:
                        receiverListResp.setTaskStatusName(SkillTaskFlowStatus.getByCode(x.getTaskStatus()).getDesc());
                        break;
                    case ASSIGN:
                        receiverListResp.setTaskStatusName(AssignTaskFlowStatus.getByCode(x.getTaskStatus()).getDesc());
                        break;
                    default:
                }
                respList.add(receiverListResp);
            });
        }
        return respList;
    }

    @Override
    public Resp<TaskProgressResp> getTaskProgress(Long id) {
        TaskProgressResp taskProgressResp = new TaskProgressResp();
        DemeterTaskUser taskUser = demeterTaskUserDao.selectByPrimaryKey(id);
        if (Objects.nonNull(taskUser)) {
            taskProgressResp.setId(taskUser.getId());
            taskProgressResp.setTaskId(taskUser.getTaskId());
            taskProgressResp.setTaskType(taskUser.getTaskType());
            taskProgressResp.setCheckoutTime(taskUser.getCheckoutTime());
            taskProgressResp.setCheckoutOpinion(taskUser.getCheckOpinion());
            taskProgressResp.setCheckoutResult(CheckoutResult.getByCode(taskUser.getCheckResult()).getDesc());
            taskProgressResp.setReceiverUid(taskUser.getReceiverUid());
            UserDetailResp userDetail = ehrComponent.getUserDetail(taskUser.getReceiverUid());
            if (Objects.nonNull(userDetail)) {
                taskProgressResp.setReceiverName(userDetail.getUserName());
            }
        } else {
            return Resp.error();
        }
        TaskFinishConditionInfoExample taskFinishConditionInfoExample = new TaskFinishConditionInfoExample();
        taskFinishConditionInfoExample.createCriteria()
                .andTaskIdEqualTo(taskUser.getTaskId())
                .andTaskTypeEqualTo(taskUser.getTaskType())
                .andUidEqualTo(taskUser.getReceiverUid());
        List<TaskFinishConditionInfo> taskFinishConditionInfos = taskFinishConditionInfoDao.selectByExample(taskFinishConditionInfoExample);
        List<Long> conditionIdList = taskFinishConditionInfos.stream().map(TaskFinishConditionInfo::getTaskFinishConditionId).collect(Collectors.toList());
        TaskFinishConditionExample taskFinishConditionExample = new TaskFinishConditionExample();
        List<TaskFinishCondition> taskFinishConditions = new ArrayList<>(16);
        if (CollectionUtils.isNotEmpty(conditionIdList)) {
            taskFinishConditionExample.createCriteria().andIdIn(conditionIdList);
            taskFinishConditions = taskFinishConditionDao.selectByExample(taskFinishConditionExample);
        }

        Map<Long, TaskFinishCondition> conditionMap = taskFinishConditions.stream().collect(Collectors.toMap(TaskFinishCondition::getId, Function.identity()));
        if (CollectionUtils.isNotEmpty(taskFinishConditionInfos)) {
            List<TaskFinishConditionInfoResp> respList = new ArrayList<>(16);
            taskFinishConditionInfos.forEach(info -> {
                TaskFinishConditionInfoResp resp = new TaskFinishConditionInfoResp();
                BeanUtils.copyProperties(info, resp);
                TaskFinishCondition taskFinishCondition = conditionMap.get(info.getTaskFinishConditionId());
                resp.setTaskConditionStatusName(TaskConditionStatus.getByCode(info.getTaskConditionStatus()).getDesc());
                resp.setTaskFinishContent(taskFinishCondition.getTaskFinishContent());
                respList.add(resp);
            });
            taskProgressResp.setTaskFinishConditionInfoRespList(respList);
            taskProgressResp.setTaskFinishConditionList(taskFinishConditions);
        }
        TaskFinishOutcomeExample taskFinishOutcomeExample = new TaskFinishOutcomeExample();
        taskFinishOutcomeExample.createCriteria()
                .andTaskIdEqualTo(taskUser.getTaskId())
                .andTaskTypeEqualTo(taskUser.getTaskType())
                .andReceiverUidEqualTo(taskUser.getReceiverUid());
        List<TaskFinishOutcome> taskFinishOutcomes = taskFinishOutcomeDao.selectByExample(taskFinishOutcomeExample);
        taskProgressResp.setTaskFinishOutcomeList(taskFinishOutcomes);
        return Resp.success(taskProgressResp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp<Object> checkTask(CheckTaskReq checkTaskReq) {
        if (TaskType.SKILL.getCode().equals(checkTaskReq.getTaskType())) {
            checkSkillForbidden(checkTaskReq.getTaskId());
        }
        if (checkTaskReq.getResult().equals(CheckoutResult.FAILED.getCode())) {
            if (StringUtils.isEmpty(checkTaskReq.getAcceptanceOpinion())) {
                return Resp.error("验收结果不通过必须填写验收意见！");
            }
        }
        Integer taskStatus = null;
        if (checkTaskReq.getResult().equals(CheckoutResult.FAILED.getCode())) {
            taskStatus = TaskType.SKILL.getCode().equals(checkTaskReq.getTaskType()) ? SkillTaskFlowStatus.FAILED.getCode() : AssignTaskFlowStatus.FAILED.getCode();
        } else if (checkTaskReq.getResult().equals(CheckoutResult.SUCCESS.getCode())) {
            taskStatus = TaskType.SKILL.getCode().equals(checkTaskReq.getTaskType()) ? SkillTaskFlowStatus.PASS.getCode() : AssignTaskFlowStatus.ACCEPTANCE.getCode();
        }

        DemeterTaskUser updateTaskUser = DemeterTaskUser.builder()
                .id(checkTaskReq.getId())
                .taskStatus(taskStatus)
                .checkoutTime(new Date())
                .checkOpinion(checkTaskReq.getAcceptanceOpinion())
                .checkResult(CheckoutResult.getByCode(checkTaskReq.getResult()).getCode())
                .modifyTime(new Date())
                .modifyId(OperatorContext.getOperator())
                .build();
        demeterTaskUserDao.updateByPrimaryKeySelective(updateTaskUser);
        return Resp.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp<Object> submitCheckTask(Long taskId, Integer taskType) {
        checkTaskAccept(taskId, taskType);
        if (TaskType.SKILL.getCode().equals(taskType)) {
            checkSkillForbidden(taskId);
        }
        checkTaskCondition(taskId, taskType);
        /// 任务无完成条件
        // 是否上传学习成果
        TaskFinishOutcomeExample taskFinishOutcomeExample = new TaskFinishOutcomeExample();
        taskFinishOutcomeExample.createCriteria()
                .andTaskIdEqualTo(taskId)
                .andTaskTypeEqualTo(taskType)
                .andReceiverUidEqualTo(OperatorContext.getOperator());
        List<TaskFinishOutcome> taskFinishOutcomes = taskFinishOutcomeDao.selectByExample(taskFinishOutcomeExample);
        if (CollectionUtils.isNotEmpty(taskFinishOutcomes)) {
            try {
                taskFinishOutcomes.forEach(outcome -> {
                    if (StringUtils.isEmpty(outcome.getFileAddress())) {
                        throw new BusinessException("无法提交验收，请先上传学习成果.");
                    }
                });
            } catch (BusinessException exception) {
                return Resp.error(exception.getMessage());
            }
        }

        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        DemeterTaskUserExample.Criteria demeterTaskUserExampleCriteria = demeterTaskUserExample.createCriteria();
        if (taskType.equals(TaskType.SKILL.getCode())) {
            demeterTaskUserExampleCriteria
                    .andTaskIdEqualTo(taskId)
                    .andTaskTypeEqualTo(taskType)
                    .andReceiverUidEqualTo(OperatorContext.getOperator());
        } else if (taskType.equals(TaskType.ASSIGN.getCode())) {
            demeterTaskUserExampleCriteria.andTaskStatusNotEqualTo(AssignTaskFlowStatus.REJECTED.getCode());
        }
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        if (CollectionUtils.isNotEmpty(demeterTaskUsers)) {
            DemeterTaskUser updateEntity = demeterTaskUsers.get(0);
            if (TaskType.getByCode(taskType).equals(TaskType.SKILL)) {
                updateEntity.setTaskStatus(SkillTaskFlowStatus.TO_AUTHENTICATE.getCode());
            } else if (TaskType.getByCode(taskType).equals(TaskType.ASSIGN)) {
                updateEntity.setTaskStatus(AssignTaskFlowStatus.WAIT_ACCEPTANCE.getCode());
            }
            demeterTaskUserDao.updateByPrimaryKey(updateEntity);
        }
        return Resp.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp<Object> submitComplete(Long taskId) {
        checkTaskAccept(taskId, TaskType.ASSIGN.getCode());
        checkTaskCondition(taskId, TaskType.ASSIGN.getCode());
        if (checkNeedCheck(taskId)) {
            throw new BusinessException("任务需要验收，无法直接完成");
        }
        DemeterTaskUserExample updateExample = new DemeterTaskUserExample();
        updateExample.createCriteria()
                .andTaskIdEqualTo(taskId)
                .andTaskTypeEqualTo(TaskType.ASSIGN.getCode())
                .andReceiverUidEqualTo(OperatorContext.getOperator());
        DemeterTaskUser update = DemeterTaskUser.builder().taskStatus(AssignTaskFlowStatus.FINISHED.getCode()).build();
        demeterTaskUserDao.updateByExampleSelective(update, updateExample);
        return Resp.success();
    }

    /**
     * 检查当前任务是否需要验收
     * @param taskId id
     */
    private boolean checkNeedCheck(Long taskId) {
        DemeterAssignTask demeterAssignTask = demeterAssignTaskDao.selectByPrimaryKey(taskId);
        return demeterAssignTask.getNeedAcceptance() == 1;
    }

    private void checkTaskCondition(Long taskId, Integer taskType) {
        // check 当前登录人 在该任务下的条件是否全部完成
        TaskFinishConditionInfoExample taskFinishConditionInfoExample = new TaskFinishConditionInfoExample();
        taskFinishConditionInfoExample.createCriteria()
                .andTaskIdEqualTo(taskId)
                .andTaskTypeEqualTo(taskType)
                .andUidEqualTo(OperatorContext.getOperator());
        List<TaskFinishConditionInfo> taskFinishConditionInfos = taskFinishConditionInfoDao.selectByExample(taskFinishConditionInfoExample);
        if (CollectionUtils.isNotEmpty(taskFinishConditionInfos)) {
            taskFinishConditionInfos.forEach(condition -> {
                if (condition.getTaskConditionStatus().equals(TaskConditionStatus.UNFINISHED.getCode())) {
                    throw new BusinessException("操作失败，有部分或全部任务条件未完成");
                }
            });
        }
    }

    @Override
    public DemeterTaskUser getRejectReason(RejectTaskReasonReq rejectTaskReasonReq) {
        return demeterTaskUserDao.selectByPrimaryKey(rejectTaskReasonReq.getId());
    }

    @Override
    public Resp<Object> getTaskDetails(Long taskId, Integer taskType) {
        // 先确定当前登录人与当前任务的关系

        // 当前登录人为接收者，指派类任务
        // 当前登录人为发布者，技能类任务
        // 当前登录人为接收者，指派类任务

        if (TaskType.ASSIGN.getCode().equals(taskType)) {
            DemeterAssignTask demeterAssignTask = demeterAssignTaskDao.selectByPrimaryKey(taskId);
            if (demeterAssignTask != null && demeterAssignTask.getPublisher().equals(OperatorContext.getOperator())) {
                // 当前登录人为发布者，指派类任务
                return Resp.success(getAssignTaskBasic(taskId));
            } else {
                // 当前登录人非发布者
                return Resp.success(getAssignTaskDetailAcceptor(taskId));
            }
        } else if (TaskType.SKILL.getCode().equals(taskType)) {
            DemeterSkillTask demeterSkillTask = demeterSkillTaskDao.selectByPrimaryKey(taskId);
            if (demeterSkillTask.getPublisher().equals(OperatorContext.getOperator())) {
                return Resp.success(getSkillDetailResp(taskId));
            } else {
                return getSkillTaskDetailAcceptor(taskId);
            }
        }
        return Resp.error();
    }

    @Override
    public Resp<TaskDetailResp> getAllDetails(Long taskId, Integer taskType) {
        TaskDetailResp resp = new TaskDetailResp();

        List<ReceiverListResp> taskCheckList = this.getTaskCheckList(taskId, taskType);

//        任务基本信息
        switch (TaskType.getByCode(taskType)) {
            case SKILL:
                SkillDetailResp skillDetailResp = this.getSkillDetailResp(taskId);
                resp.setSkillDetailResp(skillDetailResp);
                break;
            case ASSIGN:
                AssignDetailResp assignTaskBasic = this.getAssignTaskBasic(taskId);
                resp.setAssignDetailResp(assignTaskBasic);
                break;
            default:
        }

        resp.setReceiverList(taskCheckList);

        // 所有人 + 任务完成信息
        TaskFinishConditionExample taskFinishConditionExample = new TaskFinishConditionExample();
        taskFinishConditionExample.createCriteria()
                .andTaskIdEqualTo(taskId)
                .andTaskTypeEqualTo(taskType);
        List<TaskFinishCondition> taskFinishConditions = taskFinishConditionDao.selectByExample(taskFinishConditionExample);
        resp.setTaskFinishConditionList(taskFinishConditions);

        TaskFinishConditionInfoExample taskFinishConditionInfoExample = new TaskFinishConditionInfoExample();
        taskFinishConditionInfoExample.createCriteria()
                .andTaskIdEqualTo(taskId)
                .andTaskTypeEqualTo(taskType)
                .andUidEqualTo(OperatorContext.getOperator());
        List<TaskFinishConditionInfo> taskFinishConditionInfos = taskFinishConditionInfoDao.selectByExample(taskFinishConditionInfoExample);
        List<TaskFinishConditionInfoResp> taskFinishConditionInfoRespList = new ArrayList<>();
        taskFinishConditionInfos.forEach(taskFinishConditionInfo -> {
            TaskFinishConditionInfoResp infoResp = new TaskFinishConditionInfoResp();
            Long taskFinishConditionId = taskFinishConditionInfo.getTaskFinishConditionId();
            TaskFinishCondition taskFinishCondition = taskFinishConditionDao.selectByPrimaryKey(taskFinishConditionId);
            BeanUtils.copyProperties(taskFinishConditionInfo, infoResp);
            infoResp.setTaskConditionStatusName(TaskConditionStatus.getByCode(taskFinishConditionInfo.getTaskConditionStatus()).getDesc());
            if (Objects.nonNull(taskFinishCondition)) {
                infoResp.setTaskFinishContent(taskFinishCondition.getTaskFinishContent());
            }
            taskFinishConditionInfoRespList.add(infoResp);
        });
        resp.setTaskFinishConditionInfoRespList(taskFinishConditionInfoRespList);

        TaskFinishOutcomeExample taskFinishOutcomeExample = new TaskFinishOutcomeExample();
        taskFinishOutcomeExample.setDistinct(true);
        taskFinishOutcomeExample.createCriteria()
                .andTaskIdEqualTo(taskId)
                .andTaskTypeEqualTo(taskType)
                .andReceiverUidEqualTo(OperatorContext.getOperator());
        List<TaskFinishOutcome> taskFinishOutcomes = taskFinishOutcomeDao.selectByExample(taskFinishOutcomeExample);
        List<TaskFinishOutcomeResp> taskFinishOutcomeRespList = new ArrayList<>(16);
        taskFinishOutcomes.forEach(outcome -> {
            TaskFinishOutcomeResp outcomeResp = new TaskFinishOutcomeResp();
            BeanUtils.copyProperties(outcome, outcomeResp);
            taskFinishOutcomeRespList.add(outcomeResp);
        });
        resp.setTaskFinishOutcomeRespList(taskFinishOutcomeRespList);
        // 技能类任务所有人可查看关联的技能图谱信息
        return Resp.success(resp);
    }

    private AssignDetailResp getAssignTaskDetailAcceptor(Long id) {
        DemeterAssignTask entity = demeterAssignTaskDao.selectByPrimaryKey(id);
        if (Objects.isNull(entity)) {
            throw new BusinessException("system error");
        }
        AssignDetailResp detailResp = new AssignDetailResp();
        BeanUtils.copyProperties(entity, detailResp);
        List<String> receiverNameList = getReceiverListFromTaskUserEntity(entity);
        String publisherName = this.getNameFromUid(entity.getPublisher());
        detailResp.setPublisherName(publisherName);
        detailResp.setReceiverName(String.join(",", receiverNameList));
        detailResp.setTaskTypeName(TaskType.ASSIGN.getDesc());
        detailResp.setTaskNo(TaskIdPrefix.ASSIGN_PREFIX.getDesc() + entity.getId());

        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria()
                .andTaskIdEqualTo(id)
                .andTaskTypeEqualTo(TaskType.ASSIGN.getCode())
                .andReceiverUidEqualTo(OperatorContext.getOperator());
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        if (CollectionUtils.isNotEmpty(demeterTaskUsers)) {
            DemeterTaskUser demeterTaskUser = demeterTaskUsers.get(0);
            detailResp.setTaskStatus(AssignTaskFlowStatus.getByCode(demeterTaskUser.getTaskStatus()).getCode());
            detailResp.setTaskStatusName(AssignTaskFlowStatus.getByCode(demeterTaskUser.getTaskStatus()).getDesc());
        } else {
            detailResp.setTaskStatus(AssignTaskFlowStatus.UNCLAIMED.getCode());
            detailResp.setTaskStatusName(AssignTaskFlowStatus.UNCLAIMED.getDesc());

        }

        TaskFinishConditionExample taskFinishConditionExample = new TaskFinishConditionExample();
        taskFinishConditionExample.createCriteria().andTaskIdEqualTo(entity.getId());
        List<TaskFinishCondition> taskFinishConditions = taskFinishConditionDao.selectByExample(taskFinishConditionExample);

        TaskFinishConditionInfoExample taskFinishConditionInfoExample = new TaskFinishConditionInfoExample();
        taskFinishConditionInfoExample.createCriteria().andTaskIdEqualTo(entity.getId())
                .andUidEqualTo(OperatorContext.getOperator());
        List<TaskFinishConditionInfo> taskFinishConditionInfos = taskFinishConditionInfoDao.selectByExample(taskFinishConditionInfoExample);

        Map<Long, TaskFinishConditionInfo> taskFinishConditionInfoMap = taskFinishConditionInfos.stream().collect(Collectors.toMap(TaskFinishConditionInfo::getTaskFinishConditionId, Function.identity()));
        List<TaskFinishConditionInfoResp> taskFinishConditionInfoRespList = taskFinishConditions.stream().map(condition -> {
            TaskFinishConditionInfo taskFinishConditionInfo = taskFinishConditionInfoMap.get(condition.getId());
            if (Objects.nonNull(taskFinishConditionInfo)) {
                TaskFinishConditionInfoResp resp = new TaskFinishConditionInfoResp();
                BeanUtils.copyProperties(taskFinishConditionInfo, resp);
                resp.setTaskConditionStatusName(TaskConditionStatus.getByCode(taskFinishConditionInfo.getTaskConditionStatus()).getDesc());
                resp.setTaskFinishContent(condition.getTaskFinishContent());
                return resp;
            } else {
                return null;
            }
        }).collect(Collectors.toList());

        detailResp.setTaskFinishConditionList(taskFinishConditions);
        detailResp.setTaskFinishConditionInfoRespList(taskFinishConditionInfoRespList);
        TaskFinishOutcomeExample taskFinishOutcomeExample = new TaskFinishOutcomeExample();
        taskFinishOutcomeExample.createCriteria()
                .andTaskIdEqualTo(entity.getId());
        List<TaskFinishOutcome> taskFinishOutcomes = taskFinishOutcomeDao.selectByExample(taskFinishOutcomeExample);
        detailResp.setTaskFinishOutcomeList(taskFinishOutcomes);
        return detailResp;
    }

    private Resp<Object> getSkillTaskDetailAcceptor(Long id) {

        return null;
    }

    private AssignDetailResp getAssignTaskBasic(Long id) {
        DemeterAssignTask demeterAssignTask = demeterAssignTaskDao.selectByPrimaryKey(id);
        AssignDetailResp detailResp = new AssignDetailResp();
        BeanUtils.copyProperties(demeterAssignTask, detailResp);
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria()
                .andTaskIdEqualTo(id)
                .andTaskTypeEqualTo(TaskType.ASSIGN.getCode());
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        Set<String> uidSet = demeterTaskUsers.stream().map(DemeterTaskUser::getReceiverUid).collect(Collectors.toSet());
        Set<UserResp> userDetail = ehrComponent.getUserDetail(uidSet);
        if (CollectionUtils.isNotEmpty(userDetail)) {
            detailResp.setReceiverName(userDetail.stream().map(UserResp::getName).collect(Collectors.joining(",")));
        }
        detailResp.setTaskNo(TaskIdPrefix.ASSIGN_PREFIX.getDesc() + demeterAssignTask.getId());
        detailResp.setTaskType(TaskType.ASSIGN.getCode());
        detailResp.setTaskTypeName(TaskType.ASSIGN.getDesc());
        detailResp.setTaskStatusName(AssignTaskStatus.getByCode(demeterAssignTask.getTaskStatus()).getDesc());
        UserDetailResp publisher = ehrComponent.getUserDetail(demeterAssignTask.getPublisher());
        if (Objects.nonNull(publisher)) {
            detailResp.setPublisherName(publisher.getUserName());
        }
        return detailResp;
    }

    private SkillDetailResp getSkillDetailResp(Long id) {
        DemeterSkillTask demeterSkillTask = demeterSkillTaskDao.selectByPrimaryKey(id);
        SkillDetailResp detailResp = new SkillDetailResp();
        BeanUtils.copyProperties(demeterSkillTask, detailResp);
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria()
                .andTaskIdEqualTo(id)
                .andTaskTypeEqualTo(TaskType.SKILL.getCode());
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        Set<String> uidSet = demeterTaskUsers.stream().map(DemeterTaskUser::getReceiverUid).collect(Collectors.toSet());
        Set<UserResp> userDetail = ehrComponent.getUserDetail(uidSet);
        if (CollectionUtils.isNotEmpty(userDetail)) {
            detailResp.setReceiverName(userDetail.stream().map(UserResp::getName).collect(Collectors.joining(",")));
        }
        detailResp.setTaskType(TaskType.SKILL.getCode());
        detailResp.setTaskTypeName(TaskType.SKILL.getDesc());
        detailResp.setTaskNo(TaskIdPrefix.SKILL_PREFIX.getDesc() + demeterSkillTask.getId());
        detailResp.setTaskStatusName(SkillTaskStatus.getByCode(demeterSkillTask.getTaskStatus()).getDesc());
        detailResp.setSkillLevelName(SkillPointLevel.getByCode(demeterSkillTask.getSkillLevel()).getDesc());
        UserDetailResp publisher = ehrComponent.getUserDetail(demeterSkillTask.getPublisher());
        if (Objects.nonNull(publisher)) {
            detailResp.setPublisherName(publisher.getUserName());
        }
        return detailResp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp<Object> finishTaskCondition(Long conditionInfoId) {
        TaskFinishConditionInfo taskFinishConditionInfo = taskFinishConditionInfoDao.selectByPrimaryKey(conditionInfoId);
        if (taskFinishConditionInfo.getTaskType().equals(TaskType.SKILL.getCode())) {
            checkSkillForbidden(taskFinishConditionInfo.getTaskId());
        }
        if (!taskFinishConditionInfo.getUid().equals(OperatorContext.getOperator())) {
            return Resp.error("任务未认领");
        }
        checkTaskAccept(taskFinishConditionInfo.getTaskId(), taskFinishConditionInfo.getTaskType());

        if (taskFinishConditionInfo.getTaskConditionStatus().equals(TaskConditionStatus.FINISHED.getCode())) {
            return Resp.error("任务条件已完成，请勿重复完成");
        }
        // check 任务条件是否已经完成
        TaskFinishConditionInfo conditionInfo = TaskFinishConditionInfo.builder()
                .id(conditionInfoId)
                .taskConditionStatus(TaskConditionStatus.FINISHED.getCode())
                .modifyTime(new Date())
                .modifyId(OperatorContext.getOperator())
                .build();
        taskFinishConditionInfoDao.updateByPrimaryKeySelective(conditionInfo);
        return Resp.success();
    }

    /**
     * check 根据id和type查询任务是否被当前登录人认领
     */
    private void checkTaskAccept(Long id, Integer type) {
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria()
                .andTaskIdEqualTo(id)
                .andTaskTypeEqualTo(type)
                .andReceiverUidEqualTo(OperatorContext.getOperator());
        if (CollectionUtils.isEmpty(demeterTaskUserDao.selectByExample(demeterTaskUserExample))) {
            throw new BusinessException("任务未认领");
        }
    }

    @Override
    @Deprecated
    public Resp<Object> uploadAttachment(MultipartFile multipartFile, Long taskId, Integer taskType) {
        ZiroomFile ziroomFile = storageComponent.uploadFile(multipartFile);
        if (TaskType.SKILL.getCode().equals(taskType)) {
            DemeterSkillTask demeterSkillTask = new DemeterSkillTask();
            demeterSkillTask.setId(taskId);
            demeterSkillTask.setAttachmentUrl(ziroomFile.getUrl());
            demeterSkillTask.setAttachmentName(ziroomFile.getOriginal_filename());
            demeterSkillTask.setUpdateTime(new Date());
            demeterSkillTask.setModifyId(OperatorContext.getOperator());
            demeterSkillTaskDao.updateByPrimaryKeySelective(demeterSkillTask);
        } else if (TaskType.ASSIGN.getCode().equals(taskType)) {
            DemeterAssignTask demeterAssignTask = new DemeterAssignTask();
            demeterAssignTask.setId(taskId);
            demeterAssignTask.setAttachmentUrl(ziroomFile.getUrl());
            demeterAssignTask.setAttachmentName(ziroomFile.getOriginal_filename());
            demeterAssignTask.setModifyId(OperatorContext.getOperator());
            demeterAssignTask.setUpdateTime(new Date());
            demeterAssignTaskDao.updateByPrimaryKeySelective(demeterAssignTask);
        }
        return Resp.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp<Object> uploadLearningOutcome(UploadOutcomeReq uploadOutcomeReq) {
        Long taskId = uploadOutcomeReq.getTaskId();
        Integer taskType = uploadOutcomeReq.getTaskType();
        if (TaskType.SKILL.getCode().equals(taskType)) {
            checkSkillForbidden(taskId);
        }
        ZiroomFile ziroomFile = storageComponent.uploadFile(uploadOutcomeReq.getMultipartFile());
        TaskFinishOutcomeExample taskFinishOutcomeExample = new TaskFinishOutcomeExample();
        taskFinishOutcomeExample.createCriteria()
                .andReceiverUidEqualTo(OperatorContext.getOperator())
                .andTaskIdEqualTo(taskId)
                .andTaskTypeEqualTo(taskType);
        List<TaskFinishOutcome> taskFinishOutcomes = taskFinishOutcomeDao.selectByExample(taskFinishOutcomeExample);
        if (CollectionUtils.isEmpty(taskFinishOutcomes)) {
            return Resp.success("请先领取任务");
        }
        TaskFinishOutcome taskFinishOutcome = taskFinishOutcomes.get(0);
        TaskFinishOutcome update = TaskFinishOutcome.builder()
                .id(taskFinishOutcome.getId())
                .fileAddress(ziroomFile.getUrl())
                .fileName(ziroomFile.getOriginal_filename())
                .modifyTime(new Date())
                .modifyId(OperatorContext.getOperator())
                .build();
        taskFinishOutcomeDao.updateByPrimaryKeySelective(update);
        return Resp.success();
    }

    @Override
    public Boolean checkTaskDelay() {
        // 查询出所有非 已延期，待验收 验收通过 验收未通过 状态，且任务的结束时间小于当前时间的的 task_user记录，更新状态为已延期
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        List<Integer> taskEndStatus = new ArrayList<>();
        taskEndStatus.add(AssignTaskFlowStatus.WAIT_ACCEPTANCE.getCode());
        taskEndStatus.add(AssignTaskFlowStatus.ACCEPTANCE.getCode());
        taskEndStatus.add(AssignTaskFlowStatus.FAILED.getCode());
        taskEndStatus.add(AssignTaskFlowStatus.UNFINISHED.getCode());
        demeterTaskUserExample.createCriteria()
                .andTaskStatusNotIn(taskEndStatus)
                .andTaskEndTimeLessThan(new Date());
        DemeterTaskUser updated = DemeterTaskUser.builder()
                .taskStatus(AssignTaskFlowStatus.UNFINISHED.getCode())
                .build();
        demeterTaskUserDao.updateByExampleSelective(updated, demeterTaskUserExample);
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        List<Long> taskIds = demeterTaskUsers.stream().map(DemeterTaskUser::getTaskId).collect(Collectors.toList());

        // 如果有惩罚，则更新相应数据
        return true;
    }

    /**
     * 检查技能任务是否禁用
     * @param id id
     */
    private void checkSkillForbidden(Long id) {
        DemeterSkillTask demeterSkillTask = demeterSkillTaskDao.selectByPrimaryKey(id);
        if (demeterSkillTask.getTaskStatus().equals(SkillTaskStatus.FORBIDDEN.getCode())) {
            throw new BusinessException("技能任务：" + demeterSkillTask.getTaskName() + " 已被发布者禁用，无法继续操作");
        }
    }

    @Override
    public List<DemeterSkillTask> searchSkillForGraph(String condition) {
        if (condition.startsWith(TaskIdPrefix.SKILL_PREFIX.getDesc())) {
            long id;
            try {
                id = Long.parseLong(condition.substring(condition.indexOf("-") + 1));
            } catch (Exception e) {
                throw new BusinessException("请输入正确的技能任务编号.");
            }
            DemeterSkillTask demeterSkillTask = demeterSkillTaskDao.selectByPrimaryKey(id);
            return Lists.newArrayList(demeterSkillTask);
        }
        DemeterSkillTaskExample demeterSkillTaskExample = new DemeterSkillTaskExample();
        demeterSkillTaskExample.createCriteria()
                .andTaskNameLike("%" + condition + "%")
                .andTaskStatusEqualTo(SkillTaskStatus.UNFORBIDDEN.getCode());
        return demeterSkillTaskDao.selectByExample(demeterSkillTaskExample);
    }

    @Override
    public boolean submitSkillMove(Long id, Long skillTreeId) {
        DemeterSkillTask update = new DemeterSkillTask();
        update.setId(id);
        update.setSkillId(skillTreeId);
        demeterSkillTaskDao.updateByPrimaryKeySelective(update);
        return true;
    }

    @Override
    public boolean reassignTask(ReassignTaskReq reassignTaskReq) {
        // check 是否存在除了已拒绝外其他状态的任务
        List<String> reassignList = reassignTaskReq.getReassignList();
        reassignList.forEach(uid -> {
            DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
            demeterTaskUserExample.createCriteria()
                    .andTaskIdEqualTo(reassignTaskReq.getId())
                    .andTaskTypeEqualTo(TaskType.ASSIGN.getCode())
                    .andReceiverUidEqualTo(uid)
                    .andTaskStatusNotEqualTo(AssignTaskFlowStatus.REJECTED.getCode());
            List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
            if (CollectionUtils.isNotEmpty(demeterTaskUsers)) {
                throw new BusinessException("当前任务下，该接收人已存在流程中，无法重复分派任务。");
            }
        });

        DemeterAssignTask demeterAssignTask = demeterAssignTaskDao.selectByPrimaryKey(reassignTaskReq.getId());
        this.assignTask(reassignTaskReq.getReassignList(), demeterAssignTask);
        return true;
    }
}
