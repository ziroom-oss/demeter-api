package com.ziroom.tech.demeterapi.service.impl;

import com.google.common.collect.Lists;
import com.ziroom.tech.demeterapi.common.EhrComponent;
import com.ziroom.tech.demeterapi.common.OperatorContext;
import com.ziroom.tech.demeterapi.common.enums.AssignTaskFlowStatus;
import com.ziroom.tech.demeterapi.common.enums.SkillTaskFlowStatus;
import com.ziroom.tech.demeterapi.common.enums.TaskConditionStatus;
import com.ziroom.tech.demeterapi.common.enums.TaskType;
import com.ziroom.tech.demeterapi.common.exception.BusinessException;
import com.ziroom.tech.demeterapi.dao.entity.*;
import com.ziroom.tech.demeterapi.dao.mapper.*;
import com.ziroom.tech.demeterapi.po.dto.Resp;

import com.ziroom.tech.demeterapi.po.dto.req.task.AssignTaskReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.RejectTaskReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.TaskListQueryReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.SkillTaskReq;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserResp;
import com.ziroom.tech.demeterapi.po.dto.resp.task.AssignDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.task.ReleaseQueryResp;
import com.ziroom.tech.demeterapi.po.dto.resp.task.TaskFinishConditionInfoResp;
import com.ziroom.tech.demeterapi.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final static String ASSIGN_PREFIX = "ASSIGN-";
    private final static String SKILL_PREFIX = "SKILL-";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp createAssignTask(AssignTaskReq assignTaskReq) {
        DemeterAssignTask entity = new DemeterAssignTask();
        BeanUtils.copyProperties(assignTaskReq, entity);
        entity.setTaskStatus(AssignTaskFlowStatus.UNCLAIMED.getCode());
        entity.setPublisher(OperatorContext.getOperator());
        entity.setCreateId(OperatorContext.getOperator());
        entity.setModifyId(OperatorContext.getOperator());
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        demeterAssignTaskDao.insertSelective(entity);

        // DemeterTaskUser
        // batchInsert?
        String taskReceiverString = assignTaskReq.getTaskReceiver();
        if (StringUtils.isNotEmpty(taskReceiverString)) {
            Arrays.stream(taskReceiverString.split(",")).forEach(taskReceiver -> {
                DemeterTaskUser demeterTaskUser = DemeterTaskUser.builder()
                        .taskId(entity.getId())
                        .receiverUid(taskReceiver)
                        // 增加记录但为未认领状态
                        .taskStatus(AssignTaskFlowStatus.UNCLAIMED.getCode())
                        .taskType(TaskType.ASSIGN.getCode())
                        .createId(OperatorContext.getOperator())
                        .modifyId(OperatorContext.getOperator())
                        .createTime(new Date())
                        .modifyTime(new Date())
                        .build();
                demeterTaskUserDao.insertSelective(demeterTaskUser);
            });
        }

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

        return Resp.success();
    }

    @Override
    public Resp createSkillTask(SkillTaskReq skillTaskReq) {
        DemeterSkillTask entity = new DemeterSkillTask();
        BeanUtils.copyProperties(skillTaskReq, entity);
        entity.setCreateId(OperatorContext.getOperator());
        entity.setModifyId(OperatorContext.getOperator());
        entity.setCreateTime(new Date());
        entity.setModifyTime(new Date());
        List<String> taskFinishCondition = skillTaskReq.getTaskFinishCondition();
        demeterSkillTaskDao.insertSelective(entity);
        if (CollectionUtils.isNotEmpty(taskFinishCondition)) {
            taskFinishCondition.forEach(condition -> {
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
    public Resp updateAssignTask(AssignTaskReq assignTaskReq) {
        DemeterAssignTask entity = new DemeterAssignTask();
        BeanUtils.copyProperties(assignTaskReq, entity);
        entity.setModifyId(OperatorContext.getOperator());
        entity.setUpdateTime(new Date());
        demeterAssignTaskDao.updateByPrimaryKeySelective(entity);
        try {
            updateTaskFinishCondition(assignTaskReq.getTaskFinishCondition(), assignTaskReq.getId(), TaskType.ASSIGN.getCode());
        } catch (BusinessException exception) {
            log.info("update task finish condition occur exception = {}", exception.getMessage());
            return Resp.error(exception.getMessage());
        }
        return Resp.success();
    }

    // todo test
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp updateSkillTask(SkillTaskReq skillTaskReq) {
        DemeterSkillTask entity = new DemeterSkillTask();
        BeanUtils.copyProperties(skillTaskReq, entity);
        entity.setModifyId(OperatorContext.getOperator());
        entity.setModifyTime(new Date());
        demeterSkillTaskDao.updateByPrimaryKeySelective(entity);
        try {
            updateTaskFinishCondition(skillTaskReq.getTaskFinishCondition(), skillTaskReq.getId(), TaskType.SKILL.getCode());
        } catch (BusinessException exception) {
            log.info("update task finish condition occur exception = {}", exception.getMessage());
            return Resp.error(exception.getMessage());
        }
        return Resp.success();
    }

    private void updateTaskFinishCondition(List<String> newCondition, Long taskId, Integer taskType) {
        // 校验任务是否被认领
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria()
                .andTaskIdEqualTo(taskId)
                .andTaskTypeEqualTo(taskType);
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        demeterTaskUsers.forEach(x -> {
            if (TaskType.ASSIGN.getCode().equals(taskType)) {
                if (!x.getTaskStatus().equals(AssignTaskFlowStatus.UNCLAIMED.getCode())) {
                    throw new BusinessException("该指派任务已被认领，不可编辑");
                }
            } else if (TaskType.SKILL.getCode().equals(taskType)) {
//                if (!x.getTaskStatus().equals(SkillTaskFlowStatus.UNFORBIDDEN.getCode()) ||
//                        !x.getTaskStatus().equals(SkillTaskFlowStatus.FORBIDDEN.getCode())) {
//                }
                // 出现技能类任务表示任务已被认领
                throw new BusinessException("该技能任务已被认领，不可编辑");
            }
        });
        if (CollectionUtils.isNotEmpty(newCondition)) {
            // 原有任务完成条件全部删除
            TaskFinishConditionExample taskFinishConditionExample = new TaskFinishConditionExample();
            taskFinishConditionExample.createCriteria()
                    .andTaskIdEqualTo(taskId)
                    .andTaskTypeEqualTo(taskType);
            List<TaskFinishCondition> toBeDeletedTaskCondition = taskFinishConditionDao.selectByExample(taskFinishConditionExample);
            toBeDeletedTaskCondition.forEach(condition -> {
                taskFinishConditionDao.deleteByPrimaryKey(condition.getId());
            });
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
    public Resp<List<ReleaseQueryResp>> getReleaseList(TaskListQueryReq taskListQueryReq) {
        DemeterSkillTaskExample skillTaskExample = new DemeterSkillTaskExample();
        DemeterSkillTaskExample.Criteria skillTaskExampleCriteria = skillTaskExample.createCriteria();
        DemeterAssignTaskExample assignTaskExample = new DemeterAssignTaskExample();
        DemeterAssignTaskExample.Criteria assignTaskExampleCriteria = assignTaskExample.createCriteria();

        assignTaskExampleCriteria.andPublisherEqualTo(OperatorContext.getOperator());

        String receiverUid = taskListQueryReq.getSystemCode();
        List<DemeterTaskUser> demeterTaskUsers;
        if (receiverUid != null) {
            // 查询任务接收表 List<Long> taskIdList = queryTaskReceive();
            DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
            demeterTaskUserExample.createCriteria().andReceiverUidEqualTo(receiverUid);
            demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
            if (CollectionUtils.isNotEmpty(demeterTaskUsers)) {
                List<Long> taskIdList = demeterTaskUsers.stream().map(DemeterTaskUser::getTaskId).collect(Collectors.toList());
                assignTaskExampleCriteria.andIdIn(taskIdList);
                skillTaskExampleCriteria.andIdIn(taskIdList);
            } else {
                return Resp.success(Lists.newArrayList());
            }
        }

        List<ReleaseQueryResp> respList = new ArrayList<>(16);

        String nameOrNo = taskListQueryReq.getNameOrNo();
        if (Objects.nonNull(nameOrNo)) {
            if (nameOrNo.startsWith(ASSIGN_PREFIX)) {
                assignTaskExampleCriteria.andIdEqualTo(Long.parseLong(nameOrNo.split("-")[1]));
            } else if (nameOrNo.startsWith(SKILL_PREFIX)) {
                skillTaskExampleCriteria.andIdEqualTo(Long.parseLong(nameOrNo.split("-")[1]));
            } else {
                assignTaskExampleCriteria.andTaskNameLike(nameOrNo);
                skillTaskExampleCriteria.andTaskNameLike(nameOrNo);
            }
        }

        List<DemeterSkillTask> skillTasks = new ArrayList<>(16);
        List<DemeterAssignTask> assignTasks = new ArrayList<>(16);
        if (Objects.nonNull(taskListQueryReq.getTaskType())) {
            if (taskListQueryReq.getTaskType().equals(TaskType.SKILL.getCode())) {
                if (Objects.nonNull(taskListQueryReq.getTaskStatus())) {
                    skillTaskExampleCriteria.andTaskStatusEqualTo(taskListQueryReq.getTaskStatus());
                }
                skillTasks = demeterSkillTaskDao.selectByExample(skillTaskExample);
            } else if (taskListQueryReq.getTaskType().equals(TaskType.ASSIGN.getCode())) {
                if (Objects.nonNull(taskListQueryReq.getTaskStatus())) {
                    assignTaskExampleCriteria.andTaskStatusEqualTo(taskListQueryReq.getTaskStatus());
                }
                assignTasks = demeterAssignTaskDao.selectByExample(assignTaskExample);
            }
        } else {
            skillTasks = demeterSkillTaskDao.selectByExample(skillTaskExample);
            assignTasks = demeterAssignTaskDao.selectByExample(assignTaskExample);
        }

        if (CollectionUtils.isNotEmpty(skillTasks)) {
            skillTasks.forEach(task -> {
                ReleaseQueryResp releaseQueryResp = ReleaseQueryResp.builder()
                        .id(task.getId())
                        .taskNo(SKILL_PREFIX + task.getId())
                        .taskName(task.getTaskName())
                        .taskReceiverName("技能类任务接收者todo")
                        .taskStatus(task.getTaskStatus())
                        .taskStatusName(SkillTaskFlowStatus.getByCode(task.getTaskStatus()).getDesc())
                        .taskType(TaskType.SKILL.getCode())
                        .taskTypeName(TaskType.SKILL.getDesc())
                        .taskCreateTime(task.getCreateTime())
                        .growthValue(task.getSkillReward())
                        .build();
                respList.add(releaseQueryResp);
            });
        }

        if (CollectionUtils.isNotEmpty(assignTasks)) {
            assignTasks.forEach(task -> {
                List<String> receiverNameList = getReceiverListFromTaskUserEntity(task);
                ReleaseQueryResp releaseQueryResp = ReleaseQueryResp.builder()
                        .id(task.getId())
                        .taskNo(ASSIGN_PREFIX + task.getId())
                        .taskName(task.getTaskName())
                        .taskReceiverName(String.join(",", receiverNameList))
                        .taskStatus(task.getTaskStatus())
                        .taskStatusName(AssignTaskFlowStatus.getByCode(task.getTaskStatus()).getDesc())
                        .taskType(TaskType.ASSIGN.getCode())
                        .taskTypeName(TaskType.ASSIGN.getDesc())
                        .taskCreateTime(task.getCreateTime())
                        .growthValue(task.getTaskReward())
                        .build();
                respList.add(releaseQueryResp);
            });
        }

//        if (CollectionUtils.isNotEmpty(demeterTaskUsers) && CollectionUtils.isNotEmpty(respList)) {
//            List<DemeterTaskUser> finalDemeterTaskUsers = demeterTaskUsers;
//            return Resp.success(respList.stream().filter(a -> finalDemeterTaskUsers.stream()
//                    .map(DemeterTaskUser::getTaskId)
//                    .anyMatch(id -> a.getId().equals(id))).collect(Collectors.toList()));
//        }

        return Resp.success(respList);
    }

    @Override
    public Resp getExecuteList(TaskListQueryReq taskListQueryReq) {
        DemeterSkillTaskExample skillTaskExample = new DemeterSkillTaskExample();
        DemeterSkillTaskExample.Criteria skillTaskExampleCriteria = skillTaskExample.createCriteria();
        DemeterAssignTaskExample assignTaskExample = new DemeterAssignTaskExample();
        DemeterAssignTaskExample.Criteria assignTaskExampleCriteria = assignTaskExample.createCriteria();

        if (Objects.nonNull(taskListQueryReq.getSystemCode())) {

        }
        return Resp.success();
    }

    @Override
    public Resp<AssignDetailResp> getAssignDetail(Long id) {
        DemeterAssignTask entity = demeterAssignTaskDao.selectByPrimaryKey(id);
        if (Objects.isNull(entity)) {
            return Resp.error("system error");
        }
        AssignDetailResp detailResp = new AssignDetailResp();
        BeanUtils.copyProperties(entity, detailResp);
        List<String> receiverNameList = getReceiverListFromTaskUserEntity(entity);
        String publisherName = this.getNameFromUid(entity.getPublisher());
        detailResp.setPublisherName(publisherName);
        detailResp.setReceiverName(String.join(",", receiverNameList));
        detailResp.setTaskStatusName(AssignTaskFlowStatus.getByCode(entity.getTaskStatus()).getDesc());
        detailResp.setTaskTypeName(TaskType.ASSIGN.getDesc());

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
        return Resp.success(detailResp);
    }

    private List<String> getReceiverListFromTaskUserEntity(DemeterAssignTask entity) {
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria().andTaskIdEqualTo(entity.getId());
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
    public Resp getSkillDetail(Long id) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp delete(Long id, Integer taskType) {
        if (taskType.equals(TaskType.SKILL.getCode())) {
            demeterSkillTaskDao.deleteByPrimaryKey(id);
        } else if (taskType.equals(TaskType.ASSIGN.getCode())) {
            demeterAssignTaskDao.deleteByPrimaryKey(id);
        }
        return Resp.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp acceptTask(Long id, Integer type) {
        if (TaskType.ASSIGN.getCode().equals(type)) {
            DemeterAssignTask demeterAssignTask = demeterAssignTaskDao.selectByPrimaryKey(id);
            if (Objects.isNull(demeterAssignTask)) {
                return Resp.error();
            }
            this.acceptAssignTask(id);
            if (demeterAssignTask.getNeedAcceptance() == 1) {
                this.createTaskOutcome(id, type);
            }
        } else if (TaskType.SKILL.getCode().equals(type)) {
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
            // 技能类任务必须验收or认证
            this.createTaskOutcome(id, type);
        }

        // 任务完成条件
        // TaskFinishConditionInfo
        TaskFinishConditionExample taskFinishConditionExample = new TaskFinishConditionExample();
        taskFinishConditionExample.createCriteria().andTaskIdEqualTo(id);
        List<TaskFinishCondition> taskFinishConditions = taskFinishConditionDao.selectByExample(taskFinishConditionExample);
        if (CollectionUtils.isNotEmpty(taskFinishConditions)) {
            taskFinishConditions.forEach(condition -> {
                TaskFinishConditionInfo info = TaskFinishConditionInfo.builder()
                        .taskId(id)
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
                .taskType(TaskType.getByCode(type).getCode())
                .modifyId(OperatorContext.getOperator())
                .uid(OperatorContext.getOperator())
                .modifyTime(new Date())
                .build();
        taskFinishOutcomeDao.insertSelective(taskFinishOutcome);
    }

    private void acceptAssignTask(Long id) {
        // 更新DemeterTaskUser 指派类状态
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria()
                .andTaskIdEqualTo(id)
                .andReceiverUidEqualTo(OperatorContext.getOperator());
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        if (CollectionUtils.isNotEmpty(demeterTaskUsers) && demeterTaskUsers.size() == 1) {
            // 一个任务对应某个人只有一条记录
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
            log.error("task assign duplicated = {}", demeterTaskUsers);
            throw new BusinessException("task assign duplicated");
        }
    }

    private void acceptSkillTask(Long id) {
        // 技能类任务无法指派，所以如果DemeterTaskUser有了任务分配记录，说明该任务已被接收。
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria().andTaskIdEqualTo(id);
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        if (CollectionUtils.isNotEmpty(demeterTaskUsers)) {
            throw new BusinessException("重复接收技能类任务");
        }
        DemeterTaskUser entity = DemeterTaskUser.builder()
                .modifyTime(new Date())
                .createTime(new Date())
                .modifyId(OperatorContext.getOperator())
                .taskStatus(SkillTaskFlowStatus.ONGOING.getCode())
                .createId(OperatorContext.getOperator())
                .taskType(TaskType.SKILL.getCode())
                .receiverUid(OperatorContext.getOperator())
                .taskId(id)
                .build();
        demeterTaskUserDao.insertSelective(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp rejectTask(RejectTaskReq rejectTaskReq) {
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria()
                .andTaskIdEqualTo(rejectTaskReq.getTaskId());
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        if (CollectionUtils.isEmpty(demeterTaskUsers)) {
            return Resp.error();
        }
        DemeterTaskUser demeterTaskUser = demeterTaskUsers.get(0);
        // 更新DemeterTaskUser任务状态为拒绝
        DemeterTaskUser entity = DemeterTaskUser.builder()
                .id(demeterTaskUser.getId())
                .taskStatus(AssignTaskFlowStatus.FORBIDDEN.getCode())
                .build();
        demeterTaskUserDao.updateByPrimaryKeySelective(entity);
        // 拒绝原因处理
        return Resp.success();
    }

    @Override
    public Resp getAssignTaskCheckList(Long id) {
        return null;
    }

    @Override
    public Resp getSkillTaskAuthList(Long id) {
        return null;
    }
}
