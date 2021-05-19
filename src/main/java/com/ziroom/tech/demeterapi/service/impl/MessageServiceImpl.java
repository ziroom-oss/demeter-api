package com.ziroom.tech.demeterapi.service.impl;

import com.google.common.collect.Lists;
import com.ziroom.tech.demeterapi.common.EhrComponent;
import com.ziroom.tech.demeterapi.common.OperatorContext;
import com.ziroom.tech.demeterapi.common.enums.TaskType;
import com.ziroom.tech.demeterapi.common.exception.BusinessException;
import com.ziroom.tech.demeterapi.common.message.MessageSender;
import com.ziroom.tech.demeterapi.dao.entity.DemeterAssignTask;
import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillTask;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterAssignTaskDao;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterSkillTaskDao;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserResp;
import com.ziroom.tech.demeterapi.service.MessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author daijiankun
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageSender messageSender;

    @Resource
    private DemeterAssignTaskDao demeterAssignTaskDao;
    @Resource
    private DemeterSkillTaskDao demeterSkillTaskDao;

    @Resource
    private EhrComponent ehrComponent;

    @Override
    public boolean sendAssignTaskCreated(Long taskId, String taskPublisher, String taskReceiver) {
        DemeterAssignTask demeterAssignTask = demeterAssignTaskDao.selectByPrimaryKey(taskId);
        UserDetailResp userDetail = ehrComponent.getUserDetail(taskPublisher);
        if (Objects.isNull(userDetail)) {
            throw new BusinessException("ehr查询用户信息失败，uid = " + taskPublisher);
        }
        String content = userDetail.getUserName() + "为你指派任务：《" + demeterAssignTask.getTaskName() + "》，任务ID：" +  demeterAssignTask.getId() + "，请前往demeter-ui.kp.ziroom.com查看详情。";
        Set<String> receiverSet = Arrays.stream(taskReceiver.split(",")).collect(Collectors.toSet());
        Set<UserResp> userDetailSet = ehrComponent.getUserDetail(receiverSet);
        List<String> userAdCodeList = userDetailSet.stream().map(UserResp::getEmail).map(this::convertEmailToAdCode).collect(Collectors.toList());
        messageSender.send(content, userAdCodeList);
        return true;
    }

    private String convertEmailToAdCode(String email) {
        return email.split("@")[0];
    }

    @Override
    public boolean acceptTaskNotice(Long taskId, Integer taskType, String taskReceiver) {
        List<String> publisherList = new ArrayList<>();
        String content = null;
        UserDetailResp userDetail = ehrComponent.getUserDetail(taskReceiver);
        if (Objects.isNull(userDetail)) {
            throw new BusinessException("ehr查询用户信息失败，uid = " + taskReceiver);
        }

        if (TaskType.SKILL.getCode().equals(taskType)) {
            DemeterSkillTask demeterSkillTask = demeterSkillTaskDao.selectByPrimaryKey(taskId);
            publisherList.add(demeterSkillTask.getPublisher());
            content = userDetail.getUserName() + "接受了任务：《" + demeterSkillTask.getTaskName() + "》，任务ID：" + demeterSkillTask.getId() + "，请前往demeter-ui.kp.ziroom.com查看详情。";
        } else if (TaskType.ASSIGN.getCode().equals(taskType)) {
            DemeterAssignTask demeterAssignTask = demeterAssignTaskDao.selectByPrimaryKey(taskId);
            publisherList.add(demeterAssignTask.getPublisher());
            content = userDetail.getUserName() + "接受了任务：《" + demeterAssignTask.getTaskName() + "》，任务ID" + demeterAssignTask.getId() + "，请前往demeter-ui.kp.ziroom.com查看详情。";
        }
        messageSender.send(content, publisherList);
        return true;
    }

    @Override
    public boolean rejectTaskNotice(Long taskId) {
        DemeterAssignTask demeterAssignTask = demeterAssignTaskDao.selectByPrimaryKey(taskId);
        UserDetailResp userDetail = ehrComponent.getUserDetail(demeterAssignTask.getPublisher());
        if (Objects.isNull(userDetail)) {
            throw new BusinessException("ehr查询用户信息失败，uid = " + demeterAssignTask.getPublisher());
        }
        String content = userDetail.getUserName() + "拒绝了任务《" + demeterAssignTask.getTaskName() + "》，任务ID" + demeterAssignTask.getId() + "，请前往demeter-ui.kp.ziroom.com查看详情。";
        messageSender.send(content, Lists.newArrayList(demeterAssignTask.getPublisher()));
        return true;
    }

    @Override
    public boolean startCheckoutToReleaser(Long taskId, Integer taskType) {
        String content = null;
        List<String> publisherList = new ArrayList<>();
        UserDetailResp receiverDetail = ehrComponent.getUserDetail(OperatorContext.getOperator());
        if (TaskType.SKILL.getCode().equals(taskType)) {
            DemeterSkillTask demeterSkillTask = demeterSkillTaskDao.selectByPrimaryKey(taskId);
            publisherList.add(demeterSkillTask.getPublisher());
            content = receiverDetail.getUserName() + "发起任务验收，《" + demeterSkillTask.getTaskName() + "》，任务ID：" + demeterSkillTask.getId() + "，请前往growingup.ziroom.com查看。";
        } else if (TaskType.ASSIGN.getCode().equals(taskType)) {
            DemeterAssignTask demeterAssignTask = demeterAssignTaskDao.selectByPrimaryKey(taskId);
            publisherList.add(demeterAssignTask.getPublisher());
            content = receiverDetail.getUserName() + "发起任务验收，《" + demeterAssignTask.getTaskName() + "》，任务ID：" + demeterAssignTask.getId() + "，请前往growingup.ziroom.com查看。";
        }
        messageSender.send(content, publisherList);
        return false;
    }

    @Override
    public boolean startCheckoutToReceivers(Long taskId, Integer taskType) {
        String content = "您已发起任务验收，《XXXXXX》，任务ID：XXXXX，请等待发布者审核。";
        return false;
    }

    @Override
    public boolean checkoutResultNotice() {
        String content = "您的任务《XXXXXX》验收通过，任务ID：XXXXX，请前往growingup.ziroom.com查看。";
        return false;
    }

    @Override
    public boolean lighteningSkillToStaff() {
        String content = "恭喜代健坤同学完成《Java高级》技能认证，快来膜拜他吧，认证更多技能请前往growingup.ziroom.com查看。";
        return false;
    }

    @Override
    public boolean lighteningSkillToExecutor() {
        String content = "恭喜你完成《Java高级》技能认证。";
        return false;
    }

    @Override
    public boolean lighteningGraphToStaff() {
        String content = "恭喜代健坤同学完成《Java工程师技能图谱》认证，快来膜拜他吧，认证更多技能请前往growingup.ziroom.com查看。";
        return false;
    }

    @Override
    public boolean lighteningGraphToExecutor() {
        String content = "恭喜你完成《Java工程师技能图谱》认证。";
        return false;
    }
}
