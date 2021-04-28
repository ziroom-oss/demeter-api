package com.ziroom.tech.demeterapi.service.impl;

import com.google.common.collect.Lists;
import com.ziroom.tech.demeterapi.common.EhrComponent;
import com.ziroom.tech.demeterapi.common.OperatorContext;
import com.ziroom.tech.demeterapi.common.enums.AssignTaskStatus;
import com.ziroom.tech.demeterapi.common.enums.SkillTaskStatus;
import com.ziroom.tech.demeterapi.common.enums.TaskType;
import com.ziroom.tech.demeterapi.dao.entity.*;
import com.ziroom.tech.demeterapi.dao.mapper.*;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.task.AssignTaskReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.TaskListQueryReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.SkillTaskReq;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserResp;
import com.ziroom.tech.demeterapi.po.dto.resp.task.ReleaseQueryResp;
import com.ziroom.tech.demeterapi.service.TaskService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author daijiankun
 */
@Service
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
    private EhrComponent ehrComponent;

    private final static String ASSIGN_PREFIX = "ASSIGN-";
    private final static String SKILL_PREFIX = "SKILL-";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp createAssignTask(AssignTaskReq assignTaskReq) {
        DemeterAssignTask entity = new DemeterAssignTask();
        BeanUtils.copyProperties(assignTaskReq, entity);
        entity.setTaskStatus(AssignTaskStatus.UNCLAIMED.getCode());
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
                        .taskStatus(AssignTaskStatus.UNCLAIMED.getCode())
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
        int count = demeterSkillTaskDao.insertSelective(entity);
        if (count > 0) {
            return Resp.success();
        } else {
            return Resp.error("新增失败");
        }
    }

    @Override
    public Resp updateAssignTask(AssignTaskReq assignTaskReq) {
        DemeterSkillTask entity = new DemeterSkillTask();
        BeanUtils.copyProperties(assignTaskReq, entity);
        entity.setModifyId(OperatorContext.getOperator());
        entity.setModifyTime(new Date());
        int count = demeterSkillTaskDao.updateByPrimaryKeySelective(entity);
        if (count > 0) {
            return Resp.success();
        } else {
            return Resp.error("编辑任务失败");
        }
    }

    @Override
    public Resp updateSkillTask(SkillTaskReq skillTaskReq) {
        DemeterSkillTask entity = new DemeterSkillTask();
        BeanUtils.copyProperties(skillTaskReq, entity);
        entity.setModifyId(OperatorContext.getOperator());
        entity.setModifyTime(new Date());
        int count = demeterSkillTaskDao.updateByPrimaryKeySelective(entity);
        if (count > 0) {
            return Resp.success();
        } else {
            return Resp.error("编辑任务失败");
        }
    }

    @Override
    public Resp getReleaseList(TaskListQueryReq taskListQueryReq) {
        DemeterSkillTaskExample skillTaskExample = new DemeterSkillTaskExample();
        DemeterSkillTaskExample.Criteria skillTaskExampleCriteria = skillTaskExample.createCriteria();
        DemeterAssignTaskExample assignTaskExample = new DemeterAssignTaskExample();
        DemeterAssignTaskExample.Criteria assignTaskExampleCriteria = assignTaskExample.createCriteria();

        assignTaskExampleCriteria.andPublisherEqualTo(OperatorContext.getOperator());

        String receiverUid = taskListQueryReq.getSystemCode();
        List<DemeterTaskUser> demeterTaskUsers = new ArrayList<>(16);
        if (receiverUid != null) {
            // 查询任务接收表 List<Long> taskIdList = queryTaskReceive();
            DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
            demeterTaskUserExample.createCriteria().andReceiverUidEqualTo(receiverUid);
            demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
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
                        .taskStatusName(SkillTaskStatus.getByCode(task.getTaskStatus()).getDesc())
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
                DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
                demeterTaskUserExample.createCriteria().andTaskIdEqualTo(task.getId());
                List<DemeterTaskUser> demeterTaskUserList
                        = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
                Set<String> uidSet = demeterTaskUserList.stream().map(DemeterTaskUser::getReceiverUid).collect(Collectors.toSet());
                List<String> nameList = Lists.newLinkedList();
                if (CollectionUtils.isNotEmpty(uidSet)) {
                    Set<UserResp> userDetail = ehrComponent.getUserDetail(uidSet);
                    if (CollectionUtils.isNotEmpty(userDetail)) {
                        nameList = userDetail.stream().map(UserResp::getName).collect(Collectors.toList());
                    }
                }
                ReleaseQueryResp releaseQueryResp = ReleaseQueryResp.builder()
                        .id(task.getId())
                        .taskNo(ASSIGN_PREFIX + task.getId())
                        .taskName(task.getTaskName())
                        .taskReceiverName(String.join(",", nameList))
                        .taskStatus(task.getTaskStatus())
                        .taskStatusName(AssignTaskStatus.getByCode(task.getTaskStatus()).getDesc())
                        .taskType(TaskType.ASSIGN.getCode())
                        .taskTypeName(TaskType.ASSIGN.getDesc())
                        .taskCreateTime(task.getCreateTime())
                        .growthValue(task.getTaskReward())
                        .build();
                respList.add(releaseQueryResp);
            });
        }

        if (CollectionUtils.isNotEmpty(demeterTaskUsers) && CollectionUtils.isNotEmpty(respList)) {
            List<DemeterTaskUser> finalDemeterTaskUsers = demeterTaskUsers;
            return Resp.success(respList.stream().filter(a -> finalDemeterTaskUsers.stream()
                    .map(DemeterTaskUser::getTaskId)
                    .anyMatch(id -> Objects.equals(a.getId(), id))).collect(Collectors.toList()));
        }

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
    public Resp getAssignDetail(Long id) {
        return null;
    }

    @Override
    public Resp getSkillDetail(Long id) {
        return null;
    }

    @Override
    public Resp delete(Long id, Integer taskType) {
        if (taskType.equals(TaskType.SKILL.getCode())) {
            demeterSkillTaskDao.deleteByPrimaryKey(id);
        } else if (taskType.equals(TaskType.ASSIGN.getCode())) {
            demeterAssignTaskDao.deleteByPrimaryKey(id);
        }
        return Resp.success();
    }
}
