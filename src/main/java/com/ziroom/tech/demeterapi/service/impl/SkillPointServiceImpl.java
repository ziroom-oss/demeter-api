package com.ziroom.tech.demeterapi.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ziroom.tech.demeterapi.common.EhrComponent;
import com.ziroom.tech.demeterapi.common.OperatorContext;
import com.ziroom.tech.demeterapi.common.PageListResp;
import com.ziroom.tech.demeterapi.common.StorageComponent;
import com.ziroom.tech.demeterapi.common.enums.AssignTaskFlowStatus;
import com.ziroom.tech.demeterapi.common.enums.SkillTaskFlowStatus;
import com.ziroom.tech.demeterapi.common.enums.TaskIdPrefix;
import com.ziroom.tech.demeterapi.common.enums.TaskType;
import com.ziroom.tech.demeterapi.common.exception.BusinessException;
import com.ziroom.tech.demeterapi.dao.entity.*;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterRoleDao;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterSkillTaskDao;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterTaskUserDao;
import com.ziroom.tech.demeterapi.dao.mapper.RoleUserDao;
import com.ziroom.tech.demeterapi.dao.mapper.TaskFinishConditionDao;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.skill.BatchQueryReq;
import com.ziroom.tech.demeterapi.po.dto.req.skill.CheckSkillReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.SkillTaskReq;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserResp;
import com.ziroom.tech.demeterapi.po.dto.resp.storage.ZiroomFile;
import com.ziroom.tech.demeterapi.po.dto.resp.task.ReceiveQueryResp;
import com.ziroom.tech.demeterapi.po.dto.resp.task.SkillDetailResp;
import com.ziroom.tech.demeterapi.service.RoleService;
import com.ziroom.tech.demeterapi.service.SkillPointService;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author daijiankun
 */
@Service
@Slf4j
public class SkillPointServiceImpl implements SkillPointService {

    @Resource
    private DemeterSkillTaskDao demeterSkillTaskDao;
    @Resource
    private TaskFinishConditionDao taskFinishConditionDao;
    @Resource
    private DemeterTaskUserDao demeterTaskUserDao;
    @Resource
    private StorageComponent storageComponent;
    @Resource
    private RoleUserDao roleUserDao;
    @Resource
    private DemeterRoleDao demeterRoleDao;
    @Resource
    private RoleService roleService;
    @Resource
    private EhrComponent ehrComponent;


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
        entity.setSkillId(skillTaskReq.getSkillTreeId());
        entity.setSkillLevel(skillTaskReq.getSkillLevel());
        entity.setCheckRole(skillTaskReq.getCheckRoles().stream().map(String::valueOf).collect(Collectors.joining(",")));
        List<String> taskFinishCondition = skillTaskReq.getTaskFinishCondition();
        MultipartFile attachment = skillTaskReq.getAttachment();
        if (Objects.nonNull(attachment)) {
            ZiroomFile ziroomFile = storageComponent.uploadFile(attachment);
            entity.setAttachmentUrl(ziroomFile.getUrl());
            entity.setAttachmentName(ziroomFile.getOriginal_filename());
            entity.setAttachmentUuid(ziroomFile.getUuid());
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

        DemeterSkillTask demeterSkillTask = demeterSkillTaskDao.selectByPrimaryKey(skillTaskReq.getId());

        MultipartFile attachment = skillTaskReq.getAttachment();
        if (Objects.nonNull(attachment)) {
            // delete current file from ceph if exists
            String attachmentUuid = demeterSkillTask.getAttachmentUuid();
            if (Objects.nonNull(attachmentUuid)) {
                storageComponent.deleteFile(attachmentUuid);
            }

            ZiroomFile ziroomFile = storageComponent.uploadFile(attachment);
            entity.setAttachmentUrl(ziroomFile.getUrl());
            entity.setAttachmentName(ziroomFile.getOriginal_filename());
            entity.setAttachmentUuid(ziroomFile.getUuid());
        }
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
    public Map<Integer, List<DemeterSkillTask>> querySkillPointFromTreeId(List<Integer> skillTreeId) {

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
        Set<String> userId = demeterTaskUsers.stream().map(DemeterTaskUser::getReceiverUid).collect(Collectors.toSet());
        Set<String> skillPublisher = demeterSkillTasks.stream().map(DemeterSkillTask::getPublisher).collect(Collectors.toSet());
        userId.addAll(skillPublisher);
        Set<UserResp> userDetail = ehrComponent.getUserDetail(userId);
        Map<String, UserResp> userMap = userDetail.stream().collect(Collectors.toMap(UserResp::getCode, Function.identity()));

        /**
         * 查询当前登录人的角色
         */
        Map<String, List<DemeterRole>> roleMap =
                roleService.queryRoleByUid(Lists.newArrayList(OperatorContext.getOperator()));

        demeterTaskUsers.forEach(taskUser -> {
            ReceiveQueryResp resp = new ReceiveQueryResp();
            DemeterSkillTask skill = skillTaskMap.get(taskUser.getTaskId());
            List<Long> roles = Arrays.stream(skill.getCheckRole().split(",")).map(Long::parseLong).collect(Collectors.toList());
            List<Long> demeterRoles = roleMap.get(OperatorContext.getOperator()).stream().map(DemeterRole::getId).collect(Collectors.toList());
            if (!hasIntersection(roles, demeterRoles).isEmpty()) {
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
            }
        });

        respList.sort(Comparator.comparing(ReceiveQueryResp::getTaskFlowStatus));
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
}
