package com.ziroom.tech.demeterapi.service.impl;

import com.google.common.collect.Lists;
import com.ziroom.tech.demeterapi.common.EhrComponent;
import com.ziroom.tech.demeterapi.common.OperatorContext;
import com.ziroom.tech.demeterapi.common.enums.CheckoutResult;
import com.ziroom.tech.demeterapi.common.enums.TaskIdPrefix;
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

    private String convertSysCodeToAdCode(String systemCode) {
        UserDetailResp userDetail = ehrComponent.getUserDetail(systemCode);
        return convertEmailToAdCode(userDetail.getEmail());
    }

    @Override
    public boolean acceptNotice(Long taskId, Integer taskType, String skillReleaser) {
        String content = null;
        UserDetailResp userDetail = ehrComponent.getUserDetail(OperatorContext.getOperator());
        if (Objects.isNull(userDetail)) {
            throw new BusinessException("ehr查询用户信息失败，uid = " + skillReleaser);
        }

        List<String> publisherList = Lists.newArrayList(this.convertSysCodeToAdCode(skillReleaser));
        if (TaskType.SKILL.getCode().equals(taskType)) {
            DemeterSkillTask demeterSkillTask = demeterSkillTaskDao.selectByPrimaryKey(taskId);
            content = userDetail.getUserName() + "接受了技能点：《" + demeterSkillTask.getTaskName() + "》，技能点ID：" + demeterSkillTask.getId() + "，请前往demeter-ui.kp.ziroom.com查看详情。";
        } else if (TaskType.ASSIGN.getCode().equals(taskType)) {
            DemeterAssignTask demeterAssignTask = demeterAssignTaskDao.selectByPrimaryKey(taskId);
            content = userDetail.getUserName() + "接受了任务：《" + demeterAssignTask.getTaskName() + "》，任务ID：" + demeterAssignTask.getId() + "，请前往demeter-ui.kp.ziroom.com查看详情。";
        }

        messageSender.send(content, publisherList);
        return true;
    }

    @Override
    public boolean rejectTaskNotice(Long taskId) {
        DemeterAssignTask demeterAssignTask = demeterAssignTaskDao.selectByPrimaryKey(taskId);
        UserDetailResp userDetail = ehrComponent.getUserDetail(OperatorContext.getOperator());
        if (Objects.isNull(userDetail)) {
            throw new BusinessException("ehr查询用户信息失败，uid = " + demeterAssignTask.getPublisher());
        }
        String content = userDetail.getUserName() + "拒绝了任务《" + demeterAssignTask.getTaskName() + "》，任务ID:" + TaskIdPrefix.ASSIGN_PREFIX.getDesc() + "-" + demeterAssignTask.getId() + "，请前往demeter-ui.kp.ziroom.com查看详情。";
        messageSender.send(content, Lists.newArrayList(this.convertSysCodeToAdCode(demeterAssignTask.getPublisher())));
        return true;
    }

    @Override
    public boolean startCheckoutNotice(Long taskId, Integer taskType) {
        String content1 = null;
        String content2 = null;
        List<String> publisherList = new ArrayList<>();
        List<String> receiverList = new ArrayList<>();
        UserDetailResp receiverDetail = ehrComponent.getUserDetail(OperatorContext.getOperator());
        receiverList.add(convertEmailToAdCode(receiverDetail.getEmail()));
        if (TaskType.SKILL.getCode().equals(taskType)) {
            DemeterSkillTask demeterSkillTask = demeterSkillTaskDao.selectByPrimaryKey(taskId);
            publisherList.add(convertSysCodeToAdCode(demeterSkillTask.getPublisher()));
            content1 = receiverDetail.getUserName() + "发起技能点认证，《" + demeterSkillTask.getTaskName() + "》，技能点ID：" + TaskIdPrefix.SKILL_PREFIX.getDesc() + demeterSkillTask.getId() + "，请前往demeter-ui.kp.ziroom.com查看详情。";
            content2 = "您已发起技能点认证，《" + demeterSkillTask.getTaskName() + "》，技能点ID：" + TaskIdPrefix.SKILL_PREFIX.getDesc() + demeterSkillTask.getId() + "，请等待发布者审核。";
        } else if (TaskType.ASSIGN.getCode().equals(taskType)) {
            DemeterAssignTask demeterAssignTask = demeterAssignTaskDao.selectByPrimaryKey(taskId);
            publisherList.add(convertSysCodeToAdCode(demeterAssignTask.getPublisher()));
            content1 = receiverDetail.getUserName() + "发起任务验收，《" + demeterAssignTask.getTaskName() + "》，任务ID：" + TaskIdPrefix.ASSIGN_PREFIX.getDesc() + demeterAssignTask.getId() + "，请前往demeter-ui.kp.ziroom.com查看详情。";
            content2 = "您已发起任务验收，《" + demeterAssignTask.getTaskName() + "》，任务ID：" + TaskIdPrefix.ASSIGN_PREFIX.getDesc() + demeterAssignTask.getId() + "，请等待发布者审核。";
        }
        messageSender.send(content1, publisherList);
        messageSender.send(content2, receiverList);
        return false;
    }

    @Override
    public boolean checkoutResultNotice(Long taskId, Integer taskType, String receiverId, Integer result) {
        String content = null;
        List<String> receiverList = new ArrayList<>();
        receiverList.add(convertSysCodeToAdCode(receiverId));
        if (TaskType.SKILL.getCode().equals(taskType)) {
            DemeterSkillTask demeterSkillTask = demeterSkillTaskDao.selectByPrimaryKey(taskId);
            content = "您的技能点《" + demeterSkillTask.getTaskName() + "》认证" + CheckoutResult.getByCode(result).getDesc() + "，技能点ID：" + TaskIdPrefix.SKILL_PREFIX.getDesc() + demeterSkillTask.getId() + "，请前往demeter-ui.kp.ziroom.com查看详情。";
        } else if (TaskType.ASSIGN.getCode().equals(taskType)) {
            DemeterAssignTask demeterAssignTask = demeterAssignTaskDao.selectByPrimaryKey(taskId);
            content = "您的任务《" + demeterAssignTask.getTaskName() + "》" + CheckoutResult.getByCode(result).getDesc() + "，任务ID：" + TaskIdPrefix.ASSIGN_PREFIX.getDesc() + demeterAssignTask.getId() + "，请前往demeter-ui.kp.ziroom.com查看详情。";
        }
        messageSender.send(content, receiverList);
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
