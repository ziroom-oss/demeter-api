package com.ziroom.tech.demeterapi.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ziroom.tech.demeterapi.common.OperatorContext;
import com.ziroom.tech.demeterapi.common.StorageComponent;
import com.ziroom.tech.demeterapi.common.enums.TaskType;
import com.ziroom.tech.demeterapi.common.exception.BusinessException;
import com.ziroom.tech.demeterapi.dao.entity.*;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterSkillTaskDao;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterTaskUserDao;
import com.ziroom.tech.demeterapi.dao.mapper.TaskFinishConditionDao;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.skill.BatchQueryReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.SkillTaskReq;
import com.ziroom.tech.demeterapi.po.dto.resp.storage.ZiroomFile;
import com.ziroom.tech.demeterapi.po.dto.resp.task.SkillDetailResp;
import com.ziroom.tech.demeterapi.service.SkillPointService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
}
