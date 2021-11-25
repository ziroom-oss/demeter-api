package com.ziroom.tech.demeterapi.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ziroom.tech.demeterapi.common.PageListResp;
import com.ziroom.tech.demeterapi.common.UserParamThreadLocal;
import com.ziroom.tech.demeterapi.common.enums.SkillTaskFlowStatus;
import com.ziroom.tech.demeterapi.common.enums.TaskConditionStatus;
import com.ziroom.tech.demeterapi.common.enums.TaskIdPrefix;
import com.ziroom.tech.demeterapi.common.enums.TaskType;
import com.ziroom.tech.demeterapi.common.exception.BusinessException;
import com.ziroom.tech.demeterapi.dao.entity.*;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterSkillTaskDao;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterTaskUserDao;
import com.ziroom.tech.demeterapi.dao.mapper.TaskFinishConditionDao;
import com.ziroom.tech.demeterapi.dao.mapper.TaskFinishConditionInfoDao;
import com.ziroom.tech.demeterapi.dao.mapper.TaskFinishOutcomeDao;
import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.ehr.client.service.EhrServiceClient;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.skill.BatchQueryReq;
import com.ziroom.tech.demeterapi.po.dto.req.skill.CheckSkillReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.SkillTaskReq;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.task.ReceiveQueryResp;
import com.ziroom.tech.demeterapi.po.dto.resp.task.SkillDetailResp;
import com.ziroom.tech.demeterapi.service.RoleService;
import com.ziroom.tech.demeterapi.service.SkillPointService;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author daijiankun
 */
@Service
@Slf4j
public class SkillPointServiceImpl implements SkillPointService {

    @Autowired
    private DemeterSkillTaskDao demeterSkillTaskDao;

    @Autowired
    private TaskFinishConditionDao taskFinishConditionDao;

    @Autowired
    private DemeterTaskUserDao demeterTaskUserDao;

    @Autowired
    private TaskFinishConditionInfoDao taskFinishConditionInfoDao;

    @Autowired
    private TaskFinishOutcomeDao taskFinishOutcomeDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private EhrServiceClient ehrServiceClient;

    @Resource
    private TaskServiceImpl taskService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp<Object> createSkillTask(SkillTaskReq skillTaskReq) {
        DemeterSkillTask entity = new DemeterSkillTask();
        BeanUtils.copyProperties(skillTaskReq, entity);
        entity.setPublisher(skillTaskReq.getPublisher());
        entity.setCreateId(skillTaskReq.getPublisher());
        entity.setModifyId(skillTaskReq.getPublisher());
        entity.setSkillId(skillTaskReq.getSkillTreeId());
        entity.setSkillLevel(skillTaskReq.getSkillLevel());
        entity.setCheckRole(skillTaskReq.getCheckRoles().stream().map(String::valueOf).collect(Collectors.joining(",")));
        List<String> taskFinishCondition = skillTaskReq.getTaskFinishCondition();
        demeterSkillTaskDao.insertSelective(entity);
        if (CollectionUtils.isNotEmpty(taskFinishCondition)) {
            taskFinishCondition.forEach(condition -> {
                if (StringUtils.isEmpty(condition)) {
                    throw new BusinessException("任务条件内容不能为空");
                }
                TaskFinishCondition taskFinishEntity = TaskFinishCondition.builder()
                        .modifyId(skillTaskReq.getPublisher())
                        .createId(skillTaskReq.getPublisher())
                        .taskFinishContent(condition)
                        .taskType(TaskType.SKILL.getCode())
                        .taskId(entity.getId())
                        .build();
                taskFinishConditionDao.insertSelective(taskFinishEntity);
            });
        }
        return Resp.success();
    }

    @Override
    public Resp<SkillDetailResp> getSkillTask(Long id) {
        SkillDetailResp resp = new SkillDetailResp();
        DemeterSkillTask demeterSkillTask = demeterSkillTaskDao.selectByPrimaryKey(id);
        BeanUtils.copyProperties(demeterSkillTask, resp);
        resp.setCheckRoles(demeterSkillTask.getCheckRole());
        resp.setSkillTreeId(demeterSkillTask.getSkillId());
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
    public Resp<Object> updateSkillTask(SkillTaskReq skillTaskReq) {
        DemeterSkillTask entity = new DemeterSkillTask();
        BeanUtils.copyProperties(skillTaskReq, entity);
        entity.setSkillId(skillTaskReq.getSkillTreeId());
        entity.setCheckRole(skillTaskReq.getCheckRoles().stream().map(String::valueOf).collect(Collectors.joining(",")));
        entity.setModifyId(skillTaskReq.getModifyId());
        demeterSkillTaskDao.updateByPrimaryKeySelective(entity);
        try {
            updateTaskFinishCondition(skillTaskReq.getModifyId(), skillTaskReq.getTaskFinishCondition(), skillTaskReq.getId(), TaskType.SKILL.getCode());
        } catch (BusinessException exception) {
            log.info("update task finish condition occur exception = {}", exception.getMessage());
            return Resp.error(exception.getMessage());
        }
        return Resp.success();
    }

    private void updateTaskFinishCondition(String modfityId, List<String> newCondition, Long taskId, Integer taskType) {
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
                        .createId(modfityId)
                        .modifyId(modfityId)
                        .build();
                taskFinishConditionDao.insertSelective(entity);
            });
        }
    }

    @Override
    public  Map<Integer, List<DemeterSkillTask>> querySkillPointFromTreeId(List<Integer> skillTreeId) {

        DemeterSkillTaskExample demeterSkillTaskExample = new DemeterSkillTaskExample();
        if (CollectionUtils.isNotEmpty(skillTreeId)) {
            demeterSkillTaskExample.createCriteria()
                    .andSkillIdIn(skillTreeId);
            List<DemeterSkillTask> demeterSkillTasks = demeterSkillTaskDao.selectByExample(demeterSkillTaskExample);
            Map<Integer, List<DemeterSkillTask>> treeMap = demeterSkillTasks.stream()
                    .collect(Collectors.groupingBy(DemeterSkillTask::getSkillId));

            return treeMap;
        }
        return Maps.newHashMap();
    }

    @Override
    public List<DemeterTaskUser> batchQuerySkillPoints(BatchQueryReq batchQueryReq) {
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria()
                .andTaskIdIn(batchQueryReq.getSkillIds())
                .andReceiverUidEqualTo(batchQueryReq.getUid())
                .andTaskTypeEqualTo(TaskType.SKILL.getCode());
        return demeterTaskUserDao.selectByExample(demeterTaskUserExample);
    }

    @Override
    public PageListResp<ReceiveQueryResp> getSkillPointsCheckList(CheckSkillReq checkSkillReq) {

        PageListResp<ReceiveQueryResp> pageListResp = new PageListResp<>();
        List<ReceiveQueryResp> respList = new ArrayList<>(16);
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        DemeterTaskUserExample.Criteria demeterTaskUserExampleCriteria = demeterTaskUserExample.createCriteria();

        String systemCode = checkSkillReq.getSystemCode();
        if (StringUtils.isNotEmpty(systemCode)) {
            demeterTaskUserExampleCriteria.andReceiverUidEqualTo(systemCode);
        }

        Integer taskStatus = checkSkillReq.getTaskStatus();
        if (taskStatus != null && taskStatus != 0) {
            demeterTaskUserExampleCriteria.andTaskStatusEqualTo(taskStatus);
        }

        ArrayList<Integer> showStatus =
                Lists.newArrayList(SkillTaskFlowStatus.TO_AUTHENTICATE.getCode(), SkillTaskFlowStatus.PASS.getCode(),
                        SkillTaskFlowStatus.FAILED.getCode());
        demeterTaskUserExampleCriteria.andTaskTypeEqualTo(TaskType.SKILL.getCode())
                .andTaskStatusIn(showStatus);
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        List<Long> taskIds = demeterTaskUsers.stream().map(DemeterTaskUser::getTaskId).collect(Collectors.toList());

        DemeterSkillTaskExample demeterSkillTaskExample = new DemeterSkillTaskExample();
        DemeterSkillTaskExample.Criteria skillTaskExampleCriteria = demeterSkillTaskExample.createCriteria();

        String nameOrNo = checkSkillReq.getNameOrNo();
        if (StringUtils.isNotEmpty(nameOrNo)) {
            if (nameOrNo.startsWith(TaskIdPrefix.SKILL_PREFIX.getDesc())) {
                skillTaskExampleCriteria.andIdEqualTo(Long.parseLong(nameOrNo.split("-")[1]));
            } else {
                skillTaskExampleCriteria.andTaskNameLike(nameOrNo);
            }
        }

        List<DemeterSkillTask> demeterSkillTasks = new ArrayList<>(16);
        if (CollectionUtils.isNotEmpty(taskIds)) {
            skillTaskExampleCriteria.andIdIn(taskIds);
            demeterSkillTasks = demeterSkillTaskDao.selectByExample(demeterSkillTaskExample);
        }

        Map<Long, DemeterSkillTask> skillTaskMap = demeterSkillTasks.stream().collect(Collectors.toMap(DemeterSkillTask::getId, Function
                .identity()));

        List<String> userId = demeterTaskUsers.stream().map(DemeterTaskUser::getReceiverUid).collect(Collectors.toList());
        List<String> skillPublisher = demeterSkillTasks.stream().map(DemeterSkillTask::getPublisher).collect(Collectors.toList());
        userId.addAll(skillPublisher);
        ModelResult<List<UserDetailResp>> userDetailModelResult = ehrServiceClient.getUserDetail(userId);
        List<UserDetailResp> userDetail = userDetailModelResult.getResult();
        Map<String, UserDetailResp> userMap = userDetail.stream().collect(Collectors.toMap(UserDetailResp::getUserCode, Function.identity()));

        /**
         * 查询当前登录人的角色  //当前登录人 Todo
         */
        Map<String, List<DemeterRole>> roleMap =
                roleService.queryRoleByUid(Lists.newArrayList("60028724"));

        if (roleMap.size() < 1) {
            return PageListResp.emptyList();
        }

        demeterTaskUsers.forEach(taskUser -> {
            ReceiveQueryResp resp = new ReceiveQueryResp();
            DemeterSkillTask skill = skillTaskMap.get(taskUser.getTaskId());
            if(Objects.nonNull(skill)){
                List<Long> roles = Arrays.stream(skill.getCheckRole().split(",")).map(Long::parseLong).collect(Collectors.toList());
                List<Long> demeterRoles = roleMap.get("60028724").stream().map(DemeterRole::getId).collect(Collectors.toList());
                if (!hasIntersection(roles, demeterRoles).isEmpty()) {
                    BeanUtils.copyProperties(skill, resp);
                    resp.setTaskUserId(taskUser.getId());
                    resp.setTaskNo(TaskIdPrefix.SKILL_PREFIX.getDesc() + skill.getId());
                    resp.setTaskType(TaskType.SKILL.getCode());
                    resp.setTaskTypeName(TaskType.SKILL.getDesc());
                    resp.setTaskReward(skill.getSkillReward());
                    resp.setReceiver(taskUser.getReceiverUid());
                    resp.setReceiverName(userMap.get(taskUser.getReceiverUid()).getUserName());
                    resp.setPublisherName(userMap.get(skill.getPublisher()).getUserName());
                    resp.setTaskFlowStatus(taskUser.getTaskStatus());
                    resp.setTaskFlowStatusName(SkillTaskFlowStatus.getByCode(taskUser.getTaskStatus()).getDesc());
                    resp.setSubmitCheckTime(taskUser.getModifyTime());
                    respList.add(resp);
                }
            }

        });

        respList.sort(Comparator.comparing(ReceiveQueryResp::getTaskFlowStatus).thenComparing(ReceiveQueryResp::getSubmitCheckTime, Comparator.reverseOrder()));
        pageListResp.setTotal(respList.size());
        List<ReceiveQueryResp> rtv = respList.stream().skip(checkSkillReq.getStart()).limit(checkSkillReq.getPageSize()).collect(Collectors.toList());
        pageListResp.setData(rtv);
        return pageListResp;
    }

    /**
     * 交集
     * @param lists lists
     * @param <T> T
     * @return List<T>
     */
    @SafeVarargs
    private final <T> List<T> hasIntersection(List<T>... lists) {
        if (lists.length <= 0) {
            return Lists.newArrayList();
        }
        if (lists.length == 1) {
            return lists[0];
        }
        List<T> res = lists[0];
        for (List<T> list: lists) {
            res.retainAll(list);
        }
        return res;
    }

    @Override
    public Resp<Object> quickAuthReq(Long id) {
        String userId = UserParamThreadLocal.get().getUserId();
        taskService.checkSkillForbidden(id);
        DemeterSkillTask demeterSkillTask = demeterSkillTaskDao.selectByPrimaryKey(id);
        if (Objects.isNull(demeterSkillTask)) {
            return Resp.error();
        }
        try {
            taskService.acceptSkillTask(id, SkillTaskFlowStatus.TO_AUTHENTICATE.getCode());
        } catch (BusinessException exception) {
            log.error(exception.getMessage());
            return Resp.error(exception.getMessage());
        }


        //消息发送 TODO
        //messageService.acceptNotice(id, TaskType.SKILL.getCode(), demeterSkillTask.getPublisher());

        TaskFinishConditionExample taskFinishConditionExample = new TaskFinishConditionExample();
        taskFinishConditionExample.createCriteria()
                .andTaskIdEqualTo(id)
                .andTaskTypeEqualTo(TaskType.SKILL.getCode());
        List<TaskFinishCondition> taskFinishConditions = taskFinishConditionDao.selectByExample(taskFinishConditionExample);
        if (CollectionUtils.isNotEmpty(taskFinishConditions)) {
            taskFinishConditions.forEach(condition -> {
                TaskFinishConditionInfo info = TaskFinishConditionInfo.builder()
                        .taskId(id)
                        .taskType(TaskType.SKILL.getCode())
                        .taskConditionStatus(TaskConditionStatus.FINISHED.getCode())
                        .taskFinishConditionId(condition.getId())
                        .createId(userId)
                        .modifyId(userId)
                        .uid(userId)
                        .build();
                taskFinishConditionInfoDao.insertSelective(info);
            });
        }
        TaskFinishOutcome taskFinishOutcome = TaskFinishOutcome.builder()
                .createId(userId)
                .createTime(new Date())
                .taskId(id)
                .fileAddress("https://www.ziroom.com")
                .fileName("快速认证技能.jpg")
                .taskType(TaskType.SKILL.getCode())
                .modifyId(userId)
                .receiverUid(userId)
                .modifyTime(new Date())
                .build();
        taskFinishOutcomeDao.insertSelective(taskFinishOutcome);
        return Resp.success();
    }
}
