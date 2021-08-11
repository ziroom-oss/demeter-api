package com.ziroom.tech.demeterapi.service.impl;

import com.google.common.collect.Lists;
import com.ziroom.tech.demeterapi.common.EhrComponent;
import com.ziroom.tech.demeterapi.common.OperatorContext;
import com.ziroom.tech.demeterapi.common.PageListResp;
import com.ziroom.tech.demeterapi.common.StorageComponent;
import com.ziroom.tech.demeterapi.common.enums.*;
import com.ziroom.tech.demeterapi.common.exception.BusinessException;
import com.ziroom.tech.demeterapi.core.SkillNode;
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
import com.ziroom.tech.demeterapi.service.RoleService;
import com.ziroom.tech.demeterapi.service.TaskService;
import com.ziroom.tech.demeterapi.utils.DateUtils;
import com.ziroom.tech.demeterapi.utils.StringUtil;
import java.time.ZoneId;
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
    private DemeterTaskUserExtendDao demeterTaskUserExtendDao;

    @Resource
    private DemeterUserLearnManifestDao demeterUserLearnManifestDao;

    @Resource
    private DemeterSkillLearnPathDao demeterSkillLearnPathDao;

    @Resource
    private DemeterAuthHistoryDao demeterAuthHistoryDao;
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
    private SkillTreeDao skillTreeDao;

    @Resource
    private HaloService haloService;

    @Resource
    private MessageService messageService;

    @Resource
    private RoleService roleService;

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
        String taskReceiverString = String.join(",", taskReceiver);
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
        messageService.sendAssignTaskCreated(entity.getId(), OperatorContext.getOperator(), taskReceiverString);
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
                            .modifyId(OperatorContext.getOperator())
                            .modifyTime(new Date())
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
        entity.setModifyId(OperatorContext.getOperator());

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
                    update.setUpdateTime(new Date());
                    update.setModifyId(OperatorContext.getOperator());
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
                    updateForbidden.setModifyId(OperatorContext.getOperator());
                    updateForbidden.setUpdateTime(new Date());
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
                update.setUpdateTime(new Date());
                update.setModifyId(OperatorContext.getOperator());
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
                update.setUpdateTime(new Date());
                update.setModifyId(OperatorContext.getOperator());
                demeterAssignTaskDao.updateByPrimaryKeySelective(update);
            }
            return Resp.success();
        }
        return Resp.error("未知的任务类型");
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

    // TODO: 2021/6/1 split task and skill & true paging
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

        List<Integer> skillTreeId = taskListQueryReq.getSkillTreeIds();
        if (TaskType.SKILL.getCode().equals(taskListQueryReq.getTaskType())) {
            if (CollectionUtils.isNotEmpty(skillTreeId)) {
                skillTaskExampleCriteria.andSkillIdIn(skillTreeId);
            } else {
                return pageListResp;
            }
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
                SkillTree skillTree = skillTreeDao.selectByPrimaryKey(task.getSkillId());
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
                        .skillTreeName(skillTree.getName())
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
        List<ReceiveQueryResp> respList = new ArrayList<>(16);//任务列表表头、、、

        DemeterSkillTaskExample skillTaskExample = new DemeterSkillTaskExample();//DemeterSkillTask技能表
        DemeterSkillTaskExample.Criteria skillTaskExampleCriteria = skillTaskExample.createCriteria();
        DemeterAssignTaskExample assignTaskExample = new DemeterAssignTaskExample();//DemeterAssignTask任务指派表
        DemeterAssignTaskExample.Criteria assignTaskExampleCriteria = assignTaskExample.createCriteria();
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample(); //员工任务表
        DemeterTaskUserExample.Criteria demeterTaskUserCriteria = demeterTaskUserExample.createCriteria();

        DemeterTaskUserExtendExample taskUserExtendExample = new DemeterTaskUserExtendExample();//员工任务扩展
        DemeterTaskUserExtendExample.Criteria taskUserExtendCriteria = taskUserExtendExample.createCriteria();
        DemeterUserLearnManifestExample userLearnManifestExample = new DemeterUserLearnManifestExample();//员工学习清单
        DemeterUserLearnManifestExample.Criteria userLearnManifestCriteria = userLearnManifestExample.createCriteria();


        CurrentRole currentRole = this.getCurrentRole();//当前登录人角色
        String receiverCode = taskListQueryReq.getSystemCode();//获取发布人或接收人系统号
        switch (currentRole) {
            //超级管理员
            case SUPER:
                // 全部
                if (StringUtils.isNotEmpty(receiverCode)) {
                    demeterTaskUserCriteria.andReceiverUidEqualTo(receiverCode);
                }
                break;
                //部门管理员
            case DEPT:
                if (StringUtils.isNotEmpty(receiverCode)) {
                    demeterTaskUserCriteria.andReceiverUidEqualTo(receiverCode);
                } else {
                    List<String> currentDeptUsers = this.getCurrentDeptUsers();
                    demeterTaskUserCriteria.andReceiverUidIn(currentDeptUsers);
                }
                break;
                //普通用户
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

        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);//员工任务表

        List<Long> skillIds = demeterTaskUsers.stream().filter(u -> u.getTaskType().equals(TaskType.SKILL.getCode())).map(DemeterTaskUser::getTaskId).collect(Collectors.toList());
        List<Long> assignIds = demeterTaskUsers.stream().filter(u -> u.getTaskType().equals(TaskType.ASSIGN.getCode())).map(DemeterTaskUser::getTaskId).collect(Collectors.toList());

        Set<String> receiverId = demeterTaskUsers.stream().map(DemeterTaskUser::getReceiverUid).collect(Collectors.toSet());
        //技能表
        List<DemeterSkillTask> demeterSkillTasks = new ArrayList<>();
        //任务指派
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

        //ehr系统获取用户信息UserResp:code、name、email
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
                    resp.setAssigner(demeterUserLearnManifestDao.selectByPrimaryKey(demeterTaskUserExtendDao.selectByTaskUserId(taskUser.getId()).getManifestId()).getAssignerUid());
                    resp.setAssignerName(ehrComponent.getUserDetail(demeterUserLearnManifestDao.selectByPrimaryKey(demeterTaskUserExtendDao.selectByTaskUserId(taskUser.getId()).getManifestId()).getAssignerUid()).getUserName());
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

                    resp.setAssigner(taskUser.getReceiverUid());
                    resp.setAssignerName(userMap.get(taskUser.getReceiverUid()).getName());

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

    /**
     * 创建学习技能点和学习路径到指定学习清单的公共逻辑
     * @param manifestId 学习清单 id
     * @param learnerUid 接收者 uid
     * @param skillPaths 学习路径
     */
    public void createSkillLearnManifestCommon(Long manifestId, String learnerUid, Map<String, List<String>> skillPaths) {

    }

    /**
     * @param req
     * @return {@link Resp}
     * @throws
     * @author lipp3
     * @date 2021/6/30 11:06
     * @Description 分配技能学习任务 TODO
     */
    @Transactional
    @Override
    public Resp createSkillLearnManifest(CreateSkillLearnManifestReq req) {

        // 1.【demeter_user_learn_manifest】创建学习清单
        DemeterUserLearnManifest manifest = DemeterUserLearnManifest.builder()
                .assignerUid(OperatorContext.getOperator())
                .learnerUid(req.getLearnerUid())
                .name(req.getName())
                .learnPeriodStart(req.getLearnPeriodStart())
                .learnPeriodEnd(req.getLearnPeriodEnd())
                .createTime(new Date())
                .modifyTime(new Date())
                .createId(OperatorContext.getOperator())
                .modifyId(OperatorContext.getOperator())
                .build();

        demeterUserLearnManifestDao.insertSelective(manifest);
        long manifestId = manifest.getId();// 获取当前学习清单id

        // 2.创建技能点学习任务
        String learnerUid = req.getLearnerUid();
        // 先添加技能点到 demeter_task_user
        req.getSkillPaths().entrySet().forEach(entry -> {
            Long skillId = Long.valueOf(entry.getKey());
            List<String> learnPaths = entry.getValue();
            // 2.1如果技能点编号不存在，则抛出异常
            DemeterSkillTask skillTask = demeterSkillTaskDao.selectByPrimaryKey(skillId);
            if (Objects.isNull(skillTask)){
                throw new BusinessException(String.format("技能点编号：%d不存在", skillId));
            }
            // 2.2 demeter_task_user 添加技能任务
            DemeterTaskUser taskUser = this.createTaskUser(skillId, learnerUid);
            long taskUserId = taskUser.getId();
            // 2.4【demeter_task_user_extend】表 demeter_task_user_extend 关联 task_user_id 到 demeter_task_user 主键，关联 manifest_id 到 demeter_user_learn_manifest 主键
            this.createSkillTaskIntoManifest(skillId, manifestId, taskUserId);
            // 2.5【demeter_skill_learn_path】添加学习路径
            learnPaths.stream().forEach(path -> {
                this.createLearnPathIntoSkill(taskUserId, skillId, path);
            });
        });
        return Resp.success();
    }

    /**
     *
     */
    @Transactional
    @Override
    public DemeterTaskUser createTaskUser(Long taskId, String learnerUid) {
            // 2.2 如果是员工已经学习过的技能点，则抛出异常
            DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
            demeterTaskUserExample.createCriteria()
                    .andTaskTypeEqualTo(TaskType.SKILL.getCode())
                    .andReceiverUidEqualTo(learnerUid)
                    .andTaskIdEqualTo(taskId);
            List<DemeterTaskUser> taskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
            if (taskUsers.size() > 0){
                throw new BusinessException(String.format("学习者：%s已经学习了技能点：%d", learnerUid, taskId));
            }
            // 2.3【demeter_task_user】添加学习的技能点到表demeter_task_user
            DemeterTaskUser entity = DemeterTaskUser.builder()
                    .taskStatus(SkillTaskFlowStatus.ONGOING.getCode())
                    .checkResult(CheckoutResult.NEED_CHECKOUT.getCode())
                    .taskType(TaskType.SKILL.getCode())
                    .receiverUid(learnerUid)
                    .taskId(taskId)
                    .createTime(new Date())
                    .modifyTime(new Date())
                    .createId(OperatorContext.getOperator())
                    .modifyId(OperatorContext.getOperator())
                    .build();
            demeterTaskUserDao.insertSelective(entity);
            return entity;
    }

    /**
     * 修改学习清单
     */
    @Transactional
    @Override
    public Integer modifySkillLearnManifest(ModifySkillLearnManifestReq req) {
        DemeterUserLearnManifestExample manifestUpdateExample = new DemeterUserLearnManifestExample();
        manifestUpdateExample.createCriteria()
                .andIdEqualTo(req.getId());
        DemeterUserLearnManifest manifest = DemeterUserLearnManifest.builder()
                .name(req.getName())
                .modifyId(OperatorContext.getOperator())
                .learnPeriodStart(req.getLearnPeriodStart())
                .learnPeriodEnd(req.getLearnPeriodEnd())
                .learnerUid(req.getLearnerUid())
                .build();
        demeterUserLearnManifestDao.updateByExampleSelective(manifest, manifestUpdateExample);
        return 1;
    }

    @Transactional
    @Override
    public Long createLearnPathIntoSkill(Long taskUserId, Long taskId, String path) {
        DemeterSkillLearnPathExample skillLearnPathExample = new DemeterSkillLearnPathExample();
        skillLearnPathExample.createCriteria()
                .andTaskUserIdEqualTo(taskUserId)
                .andTaskIdEqualTo(taskId)
                .andPathEqualTo(path);
        DemeterSkillLearnPath learnPath = DemeterSkillLearnPath.builder()
                .taskUserId(taskUserId)
                .taskId(taskId)
                .path(path)
                .createTime(new Date())
                .modifyTime(new Date())
                .createId(OperatorContext.getOperator())
                .modifyId(OperatorContext.getOperator())
                .build();
        demeterSkillLearnPathDao.insertSelective(learnPath);
        return learnPath.getId();
    }

    @Transactional
    @Override
    public Integer createSkillTaskIntoManifest(Long taskId, Long manifestId, Long taskUserId) {
        DemeterTaskUserExtend userExtend = DemeterTaskUserExtend.builder()
                .taskUserId(taskUserId)
                .taskId(taskId)
                .manifestId(manifestId)
                .createTime(new Date())
                .modifyTime(new Date())
                .createId(OperatorContext.getOperator())
                .modifyId(OperatorContext.getOperator())
                .build();
        return demeterTaskUserExtendDao.insertSelective(userExtend);
    }

    /**
     * 移除学习清单中的技能点
     * 在编辑学习清单的时候，会为技能点添加学习路径 Skill_Learn_Path 表
     * 所以在移除技能点的话也得去移除属下的所有学习路径
     */
    @Transactional
    @Override
    public Integer deleteSkillLearnManifestSkill(Long manifestId, Long taskId) {
        // 先移除 Task_User_Extend 表的记录
        DemeterTaskUserExtendExample taskUserExtendExample = new DemeterTaskUserExtendExample();
        taskUserExtendExample.createCriteria()
                .andManifestIdEqualTo(manifestId)
                .andTaskIdEqualTo(taskId);
        DemeterTaskUserExtend taskUserExtend = DemeterTaskUserExtend.builder()
                .isDel((byte)1)
                .build();
        demeterTaskUserExtendDao.updateByExampleSelective(taskUserExtend, taskUserExtendExample);

        /**
         * 移除该清单下指定技能点关联的学习路径
         * 先批量查询有相同 taskId 和 manifestId 的 taskUserExtend
         * 然后遍历 taskUserExtend 拿到 taskUserId 与 taskId 组合条件查询学习路径并批量设置 isDel = 1
         */
        List<DemeterTaskUserExtend> taskUserExtends = demeterTaskUserExtendDao.selectByExample(taskUserExtendExample);
        taskUserExtends.stream().forEach(item -> {
            DemeterSkillLearnPathExample skillLearnPathExample = new DemeterSkillLearnPathExample();
            skillLearnPathExample.createCriteria()
                    .andTaskIdEqualTo(taskId)
                    .andTaskUserIdEqualTo(item.getTaskUserId());
            DemeterSkillLearnPath skillLearnPath = DemeterSkillLearnPath.builder().isDel((byte)1).build();
            demeterSkillLearnPathDao.updateByExampleSelective(skillLearnPath, skillLearnPathExample);
        });
        return 1;
    }

    @Override
    public Integer deleteSkillLearnPath(Long id) {
        DemeterSkillLearnPathExample LearnPathExample = new DemeterSkillLearnPathExample();
        LearnPathExample.createCriteria()
                .andIdEqualTo(id);
        DemeterSkillLearnPath learnPath = DemeterSkillLearnPath.builder().isDel((byte)1).build();
        return demeterSkillLearnPathDao.updateByExampleSelective(learnPath, LearnPathExample);
    }

    /**
     * 查询分配技能学习清单
     *
     * @param req 任务列表查询请求体
     * @return Resp
     */
    @Override
    public PageListResp<SkillLearnManifestResp> getSkillLearnManifest(GetSkillLearnManifestReq req) {
        PageListResp<SkillLearnManifestResp> pageListResp = new PageListResp<>();
        //1.【demeter_user_learn_manifest】根据清单名称或清单编号查询demeter_user_learn_manifest表
        DemeterUserLearnManifestExample example = new DemeterUserLearnManifestExample();
        DemeterUserLearnManifestExample.Criteria criteria = example.createCriteria();
        // 模糊查询清单名称
        if (StringUtils.isNotEmpty(req.getManifestIdOrName())){
            criteria.andNameLike("%" + req.getManifestIdOrName() + "%");
        }
        List<DemeterUserLearnManifest> manifests = demeterUserLearnManifestDao.selectByExample(example);
        // 如果清单名称是 long 类型则移除上述查询条件，并将其赋值到 id 重新查询
        if (manifests.size() == 0 && StringUtil.isLong(req.getManifestIdOrName())){
            example.clear();
            example.createCriteria().andIdEqualTo(Long.valueOf(req.getManifestIdOrName()));
            manifests = demeterUserLearnManifestDao.selectByExample(example);
        }
        if (manifests.size() == 0){
            return pageListResp;
        }

        //2.【demeter_task_user_extend】根据demeter_user_learn_manifest主键id关联demeter_task_user_extend的manifest_id查询清单下面的所有task_Id
        List<SkillLearnManifestResp> manifestResps = new ArrayList<>();
        manifests.stream().forEach(manifest -> {
            SkillLearnManifestResp skillLearnManifestResp = new SkillLearnManifestResp();
            BeanUtils.copyProperties(manifest, skillLearnManifestResp);

            // uid 查询分配者的姓名
            if (StringUtils.isNotEmpty(manifest.getAssignerUid())) { //分配者不为空
                UserDetailResp userDetail = ehrComponent.getUserDetail(manifest.getAssignerUid());
                if (Objects.nonNull(userDetail)) {
                    skillLearnManifestResp.setAssignerName(userDetail.getUserName());
                }
            }else{
                skillLearnManifestResp.setAssignerName("-");
            }

            // 查询学习者的姓名
            if(StringUtils.isNotEmpty(manifest.getLearnerUid())){
                UserDetailResp userDetail = ehrComponent.getUserDetail(manifest.getLearnerUid());
                if (Objects.nonNull(userDetail)) {
                    skillLearnManifestResp.setLearnerName(userDetail.getUserName());
                }
            }

            // 清单 id 关联到 taskUserExtend 表的查询
            DemeterTaskUserExtendExample extendExample = new DemeterTaskUserExtendExample();
            extendExample.createCriteria()
                    .andManifestIdEqualTo(manifest.getId());
            List<DemeterTaskUserExtend> taskUserExtends = demeterTaskUserExtendDao.selectByExample(extendExample);


            boolean passed = true;
            // 从 extend 表拿到 taskUserId 到 taskUser 表批量查询任务状态和任务类型
            for(DemeterTaskUserExtend extend : taskUserExtends){
                DemeterTaskUser demeterTaskUser = demeterTaskUserDao.selectByPrimaryKey(extend.getTaskUserId());
                skillLearnManifestResp.setTaskType(demeterTaskUser.getTaskType());
                // 如果 taskUser 的任务状态不是 PASS
                if (demeterTaskUser.getTaskStatus() != SkillTaskFlowStatus.PASS.getCode()){
                    passed = false;
                    break;
                }
            }

            // 初始化清单流程状态为进行中，如果清单内所有任务的状态都是 PASS 则该清单判断为通过
            SkillManifestFlowStatus status = SkillManifestFlowStatus.ONGOING;
            if (passed){
                status = SkillManifestFlowStatus.PASS;
            }
            skillLearnManifestResp.setStatus(status.getCode());
            skillLearnManifestResp.setStatusName(status.getName());

            // 如果查询清单状态是通过的话
            if (req.getStatus().equals(SkillManifestFlowStatus.PASS.getCode())){
                // 并且清单状态（包含的任务也都是通过状态）
                if (passed){
                    manifestResps.add(skillLearnManifestResp);
                }
             } else {
                // 如果查询的是其它状态
                if (!passed) {
                    manifestResps.add(skillLearnManifestResp);
                }
            }
        });

        // 因为 manifestResps 内综合除分页以外的结果
        List<SkillLearnManifestResp> resps = manifestResps.stream().sorted(Comparator.comparing(SkillLearnManifestResp::getCreateTime).reversed()).skip(req.getStart()).limit(req.getPageSize()).collect(Collectors.toList());
        pageListResp.setTotal(manifestResps.size());
        pageListResp.setData(resps);

        return pageListResp;
    }

    /**
     * @param manifestId 清单id
     * @return {@link Resp}
     * @throws
     * @author modified by zhangxt3
     * @date 2021/7/1 9:24
     * @Description TODO
     */
    @Override
    public SkillLearnManifestDetailResp getSkillLearnManifestDetail(Long manifestId) {
        DemeterUserLearnManifest manifest = demeterUserLearnManifestDao.selectByPrimaryKey(manifestId);
        if (Objects.isNull(manifest)){
            return null;
        }
        // 学习清单基本信息
        SkillLearnManifestDetailResp detailResp = new SkillLearnManifestDetailResp();
        BeanUtils.copyProperties(manifest, detailResp);
        detailResp.setLearnPeriod(
                DateUtils.FORMATTER_YYYY_MM_DD.format(manifest.getLearnPeriodStart().toInstant().atZone(ZoneId.systemDefault())) + " 至 " +
                        DateUtils.FORMATTER_YYYY_MM_DD.format(manifest.getLearnPeriodEnd().toInstant().atZone(ZoneId.systemDefault())));
        //根据人物uid查出姓名
        detailResp.setAssignerName(ehrComponent.getUserDetail(detailResp.getAssignerUid()).getUserName());
        detailResp.setLearnerName(ehrComponent.getUserDetail(detailResp.getLearnerUid()).getUserName());
//        detailResp.setSkillTree(getManifestSkillGrape(manifestId).getSkillTree());
        //1.根据manifestId获取所有demeter_task_user_extend
        DemeterTaskUserExtendExample extendExample = new DemeterTaskUserExtendExample();
        extendExample.createCriteria()
                .andManifestIdEqualTo(manifestId);
        List<DemeterTaskUserExtend> taskUserExtends = demeterTaskUserExtendDao.selectByExample(extendExample);
        List<SkillTaskUserExtend> demeterSkillTasks = new ArrayList<>();
        taskUserExtends.stream().forEach(extend -> {
            if (extend.getIsDel() < 1) {
                DemeterSkillTask demeterSkillTask = demeterSkillTaskDao.selectByPrimaryKey(extend.getTaskId());
                SkillTaskUserExtend skillTaskUserExtend = new SkillTaskUserExtend();
                skillTaskUserExtend.setTaskUserId(extend.getTaskUserId());
                BeanUtils.copyProperties(demeterSkillTask, skillTaskUserExtend);
                demeterSkillTasks.add(skillTaskUserExtend);
            }
        });

        /**
         * 通过技能任务查询关联的学习路径
         * 技能任务（点）的 id 对应学习任务的 taskId
         */
        List<DemeterSkillLearnPath> demeterSkillLearnPathResp = new ArrayList<>();
        demeterSkillTasks.stream().forEach(skillTask -> {
           DemeterSkillLearnPathExample learnPathExample = new DemeterSkillLearnPathExample();
           learnPathExample.createCriteria()
                   .andTaskIdEqualTo(skillTask.getId())
                   .andTaskUserIdEqualTo(skillTask.getTaskUserId());
           List<DemeterSkillLearnPath> demeterSkillLearnPaths = demeterSkillLearnPathDao.selectByExample(learnPathExample);
           demeterSkillLearnPaths.stream().forEach(item -> {
               if (item.getIsDel() < 1) {
                   demeterSkillLearnPathResp.add(item);
               }
           });
        });
        long count = demeterSkillTasks.stream()
                        .filter(o -> o.getTaskStatus().equals(SkillTaskFlowStatus.PASS.getCode()))
                        .count();
        if (demeterSkillTasks.size() == count) {
            detailResp.setStatus(SkillManifestFlowStatus.PASS.getCode());
            detailResp.setStatusName(SkillManifestFlowStatus.PASS.getName());
        } else {
            detailResp.setStatus(SkillManifestFlowStatus.ONGOING.getCode());
            detailResp.setStatusName(SkillManifestFlowStatus.ONGOING.getName());
        }
        long sum = demeterSkillTasks.stream().mapToLong(DemeterSkillTask::getSkillReward).sum();
        detailResp.setReward(sum);
        detailResp.setDemeterSkillTasks(demeterSkillTasks);
        detailResp.setDemeterSkillLearnPaths(demeterSkillLearnPathResp);
        return detailResp;
    }

    /**
     * @param manifestId
     * @return {@link Resp<ManifestSkillLearnGrapeResp>}
     * @throws
     * @author
     * @date 2021/7/1 11:10
     * @Description 根据清单id获取技能学习图谱  TODO
     */
    @Override
    public ManifestSkillLearnGrapeResp getManifestSkillGrape(Long manifestId) {
        ManifestSkillLearnGrapeResp learnGrapeResp = new ManifestSkillLearnGrapeResp();

        //1.根据manifestId获取所有demeter_task_user_extend
        DemeterTaskUserExtendExample extendExample = new DemeterTaskUserExtendExample();
        extendExample.createCriteria()
                .andManifestIdEqualTo(manifestId);
        List<DemeterTaskUserExtend> taskUserExtends = demeterTaskUserExtendDao.selectByExample(extendExample);

        taskUserExtends.stream().forEach(extend -> {
            Long taskId = extend.getTaskUserId();
            Long skillId = extend.getTaskId();
            //2.获取技能层次结构
            SkillHierarchyResp skillHierarchy = getSkillHierarchy(skillId);
            SkillHierarchyResp skillParent = skillHierarchy;
            SkillNode rootNode = null;
            SkillNode childNode = null;
            while (Objects.nonNull(skillParent)){
                Long id = skillParent.getId();
                Long parentId = skillParent.getParentId();
                String name = skillParent.getName();
                if (Objects.isNull(rootNode)) {
                    rootNode = new SkillNode(id, parentId, name);
                    childNode = rootNode;
                }else{
                    childNode = childNode.getOrAddChild(new SkillNode(parentId, id, name));
                }

                skillParent = skillParent.getChild();
            }
            if (Objects.isNull(childNode)){
                return;
            }

            DemeterSkillLearnPathExample learnPathExample = new DemeterSkillLearnPathExample();
            learnPathExample.createCriteria()
                    .andTaskUserIdEqualTo(taskId);
            //3.获取技能点学习路径
            List<DemeterSkillLearnPath> skillLearnPaths = demeterSkillLearnPathDao.selectByExample(learnPathExample);
            for (DemeterSkillLearnPath tmp : skillLearnPaths){
                childNode.addChild(new SkillNode(childNode.getId(), null, tmp.getPath()));
            }
            learnGrapeResp.getSkillTree().addBranch(rootNode);
        });

        return learnGrapeResp;
    }

    /**
     * @param skillId
     * @return {@link SkillHierarchyResp}
     * @throws
     * @author
     * @date 2021/7/1 10:49
     * @Description 获取技能点的层级结构
     */
    @Override
    public SkillHierarchyResp getSkillHierarchy(Long skillId) {
        //1.获取技能点信息
        DemeterSkillTask skillTask = demeterSkillTaskDao.selectByPrimaryKey(skillId);
        Integer parentId = skillTask.getSkillId();
        SkillHierarchyResp child = new SkillHierarchyResp();
        SkillHierarchyResp parent = null;
        child.setId(skillId);
        child.setName(skillTask.getTaskName());
        child.setParentId(Long.valueOf(parentId));

        //2.获取技能树信息
        while (parentId != 0) {
            SkillTree skillTree = skillTreeDao.selectByPrimaryKey(parentId);
            parentId = skillTree.getParentId();
            parent = new SkillHierarchyResp();
            parent.setId(Long.valueOf(skillTree.getId()));
            parent.setName(skillTree.getName());
            parent.setParentId(Long.valueOf(parentId));
            child.setParent(parent);
            parent.setChild(child);
            child = parent;
        }

        return parent;
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
            messageService.acceptNotice(id, type, demeterAssignTask.getPublisher());
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
            messageService.acceptNotice(id, type, demeterSkillTask.getPublisher());
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
        messageService.rejectTaskNotice(rejectTaskReq.getTaskId());
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
            taskProgressResp.setCheckoutTime(taskUser.getCheckoutTime()); //检查时间
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

        DemeterAuthHistoryExample demeterAuthHistoryExample = new DemeterAuthHistoryExample();
        demeterAuthHistoryExample.createCriteria()
                .andUserTaskIdEqualTo(id);
        List<DemeterAuthHistory> demeterAuthHistories =
                demeterAuthHistoryDao.selectByExample(demeterAuthHistoryExample);
        Set<String> uidSet =
                demeterAuthHistories.stream().map(DemeterAuthHistory::getAuthUser).collect(Collectors.toSet());
        Set<UserResp> userDetail = ehrComponent.getUserDetail(uidSet);
        Map<String, UserResp> userMap =
                userDetail.stream().collect(Collectors.toMap(UserResp::getCode, Function.identity()));
        if (CollectionUtils.isNotEmpty(demeterAuthHistories)) {
            List<AuthHistoryResp> authHistories = new ArrayList<>(16);
            demeterAuthHistories.forEach(history -> {
                AuthHistoryResp resp = new AuthHistoryResp();
                BeanUtils.copyProperties(history, resp);
                resp.setAuthResultName(CheckoutResult.getByCode(history.getAuthResult()).getDesc());
                Optional<UserResp> userResp = Optional.ofNullable(userMap.get(history.getAuthUser()));
                resp.setAuthUserName(userResp.map(UserResp::getName).orElse("Unknown"));
                authHistories.add(resp);
            });
            taskProgressResp.setTaskAuthHistoryList(authHistories);
        }

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

        DemeterAuthHistory demeterAuthHistory = DemeterAuthHistory.builder()
                .userTaskId(checkTaskReq.getId())
                .authOpinion(checkTaskReq.getAcceptanceOpinion())
                .authResult(checkTaskReq.getResult())
                .authUser(OperatorContext.getOperator())
                .createId(OperatorContext.getOperator())
                .modifyId(OperatorContext.getOperator())
                .createTime(new Date())
                .modifyTime(new Date())
                .build();
        demeterAuthHistoryDao.insertSelective(demeterAuthHistory);

        messageService.checkoutResultNotice(checkTaskReq.getTaskId(), checkTaskReq.getTaskType(), checkTaskReq.getReceiverUid(), checkTaskReq.getResult());
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
            messageService.startCheckoutNotice(taskId, TaskType.SKILL.getCode());
        } else if (taskType.equals(TaskType.ASSIGN.getCode())) {
            demeterTaskUserExampleCriteria
                    .andTaskIdEqualTo(taskId)
                    .andTaskTypeEqualTo(taskType)
                    .andReceiverUidEqualTo(OperatorContext.getOperator())
                    .andTaskStatusNotEqualTo(AssignTaskFlowStatus.REJECTED.getCode());
            messageService.startCheckoutNotice(taskId, TaskType.ASSIGN.getCode());
        }
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        if (CollectionUtils.isNotEmpty(demeterTaskUsers)) {
            DemeterTaskUser updateEntity = demeterTaskUsers.get(0);
            updateEntity.setModifyTime(new Date());
            updateEntity.setModifyId(OperatorContext.getOperator());
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
        DemeterTaskUser update = DemeterTaskUser.builder()
                .taskStatus(AssignTaskFlowStatus.FINISHED.getCode())
                .modifyId(OperatorContext.getOperator())
                .modifyTime(new Date())
                .build();
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
        detailResp.setSkillTreeId(demeterSkillTask.getSkillId());
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
        String checkRole = demeterSkillTask.getCheckRole();
        if (StringUtils.isNotEmpty(checkRole)) {
            List<Long> roleIds = Arrays.stream(checkRole.split(",")).map(Long::parseLong).collect(Collectors.toList());
            List<DemeterRole> demeterRoles = roleService.batchQueryByIds(roleIds);
            detailResp.setCheckRole(demeterRoles);
            List<RoleUser> roleUsers = roleService.batchQueryUserByIds(roleIds);
            if (CollectionUtils.isNotEmpty(roleUsers)) {
                List<String> roleUserList = roleUsers.stream().map(RoleUser::getSystemCode).collect(Collectors.toList());
                detailResp.setCheckRoleUserList(roleUserList);
                Set<UserResp> userRespSet = ehrComponent.getUserDetail(new HashSet<>(roleUserList));
                if (CollectionUtils.isNotEmpty(userRespSet)) {
                    detailResp.setCheckRoleUserNameList(userRespSet.stream().map(UserResp::getName).collect(Collectors.toList()));
                }
            }
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
                .modifyTime(new Date())
                .modifyId(OperatorContext.getOperator())
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
    public boolean submitSkillMove(Long id, Integer skillTreeId) {
        DemeterSkillTask update = new DemeterSkillTask();
        update.setId(id);
//        update.setSkillId(skillTreeId);
        update.setModifyId(OperatorContext.getOperator());
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
