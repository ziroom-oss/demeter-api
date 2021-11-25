package com.ziroom.tech.demeterapi.service.impl;

import com.alibaba.fastjson.JSON;
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
import com.ziroom.tech.demeterapi.message.channel.console.handler.ConsoleSendHandler;
import com.ziroom.tech.demeterapi.message.channel.console.model.ConsoleMessageModel;
import com.ziroom.tech.demeterapi.message.event.ConsoleMessageChannelEvent;
import com.ziroom.tech.demeterapi.open.ehr.client.service.EhrServiceClient;
import com.ziroom.tech.demeterapi.open.model.ModelResult;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserResp;
import com.ziroom.tech.demeterapi.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 此处 ConsoleMessageChannelEvent 消息事件 需要替换为自己的
 * 当前测试消息发送到控制台
 * @author daijiankun
 */
@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    @Resource
    private DemeterAssignTaskDao demeterAssignTaskDao;
    @Resource
    private DemeterSkillTaskDao demeterSkillTaskDao;

    @Autowired
    private EhrServiceClient ehrServiceClient;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public boolean sendAssignTaskCreated(Long taskId, String taskPublisher, String taskReceiver) {
        DemeterAssignTask demeterAssignTask = demeterAssignTaskDao.selectByPrimaryKey(taskId);
        ModelResult<UserDetailResp> userInfoModelResult = ehrServiceClient.getUserInfo(taskPublisher);
        if (!userInfoModelResult.isSuccess()) {
            throw new BusinessException("ehr查询用户信息失败，uid = " + taskPublisher);
        }
        UserDetailResp userDetail = userInfoModelResult.getResult();
        String content = userDetail.getUserName() + "为你指派任务：《" + demeterAssignTask.getTaskName() + "》，任务ID：" +  demeterAssignTask.getId() + "，请前往demeter-ui.kp.ziroom.com查看详情。";
        Set<String> receiverSet = Arrays.stream(taskReceiver.split(",")).collect(Collectors.toSet());

        ModelResult<List<UserDetailResp>> modelResult = ehrServiceClient.getUserDetail(new ArrayList<>(receiverSet));
        if(!modelResult.isSuccess()){
            throw new BusinessException("ehr查询用户信息失败，taskReceiver = " + taskReceiver);
        }
        List<UserDetailResp> userDetailList = modelResult.getResult();
        List<String> userCodes = userDetailList.stream().map(UserDetailResp::getUserCode).collect(Collectors.toList());

        ConsoleMessageModel consoleMessageModel = new ConsoleMessageModel();
        consoleMessageModel.setToUser(userCodes);
        consoleMessageModel.setContext(content);
        this.sendMsg(consoleMessageModel);
        return true;
    }

    @Override
    public boolean acceptNotice(Long taskId, Integer taskType, String skillReleaser) {
        String content = null;
        ModelResult<UserDetailResp> userInfoModelResult = ehrServiceClient.getUserInfo(OperatorContext.getOperator());
        if (!userInfoModelResult.isSuccess()) {
            throw new BusinessException("ehr查询用户信息失败，uid = " + OperatorContext.getOperator());
        }
        UserDetailResp userDetail = userInfoModelResult.getResult();

        ModelResult<UserDetailResp> publisherModelResult = ehrServiceClient.getUserInfo(skillReleaser);
        if (!publisherModelResult.isSuccess()) {
            throw new BusinessException("ehr查询用户信息失败，uid = " + skillReleaser);
        }

        UserDetailResp publisherModel = publisherModelResult.getResult();
        List<String> publisherList = Lists.newArrayList(publisherModel.getUserCode());

        if (TaskType.SKILL.getCode().equals(taskType)) {
            DemeterSkillTask demeterSkillTask = demeterSkillTaskDao.selectByPrimaryKey(taskId);
            content = userDetail.getUserName() + "接受了技能点：《" + demeterSkillTask.getTaskName() + "》，技能点ID：" + demeterSkillTask.getId() + "，请前往demeter-ui.kp.ziroom.com查看详情。";
        } else if (TaskType.ASSIGN.getCode().equals(taskType)) {
            DemeterAssignTask demeterAssignTask = demeterAssignTaskDao.selectByPrimaryKey(taskId);
            content = userDetail.getUserName() + "接受了任务：《" + demeterAssignTask.getTaskName() + "》，任务ID：" + demeterAssignTask.getId() + "，请前往demeter-ui.kp.ziroom.com查看详情。";
        }

        ConsoleMessageModel consoleMessageModel = new ConsoleMessageModel();
        consoleMessageModel.setToUser(publisherList);
        consoleMessageModel.setContext(content);

        this.sendMsg(consoleMessageModel);
        return true;
    }

    @Override
    public boolean rejectTaskNotice(Long taskId) {
        DemeterAssignTask demeterAssignTask = demeterAssignTaskDao.selectByPrimaryKey(taskId);

        ModelResult<UserDetailResp> userModelResult = ehrServiceClient.getUserInfo(OperatorContext.getOperator());
        if (!userModelResult.isSuccess()) {
            throw new BusinessException("ehr查询用户信息失败，uid = " + OperatorContext.getOperator());
        }

        UserDetailResp userDetail = userModelResult.getResult();

        ModelResult<UserDetailResp> publisherModelResult = ehrServiceClient.getUserInfo(demeterAssignTask.getPublisher());
        if (!publisherModelResult.isSuccess()) {
            throw new BusinessException("ehr查询用户信息失败，uid = " + demeterAssignTask.getPublisher());
        }

        UserDetailResp publisherModel = publisherModelResult.getResult();
        List<String> publisherList = Lists.newArrayList(publisherModel.getUserCode());

        String content = userDetail.getUserName() + "拒绝了任务《" + demeterAssignTask.getTaskName() + "》，任务ID:" + TaskIdPrefix.ASSIGN_PREFIX.getDesc() + "-" + demeterAssignTask.getId() + "，请前往demeter-ui.kp.ziroom.com查看详情。";

        ConsoleMessageModel consoleMessageModel = new ConsoleMessageModel();
        consoleMessageModel.setToUser(publisherList);
        consoleMessageModel.setContext(content);

        this.sendMsg(consoleMessageModel);
        return true;
    }

    @Override
    public boolean startCheckoutNotice(Long taskId, Integer taskType) {
        String content1 = null;
        String content2 = null;
        List<String> publisherList = new ArrayList<>();
        List<String> receiverList = new ArrayList<>();

        ModelResult<UserDetailResp> userModelResult = ehrServiceClient.getUserInfo(OperatorContext.getOperator());
        if (!userModelResult.isSuccess()) {
            throw new BusinessException("ehr查询用户信息失败，uid = " + OperatorContext.getOperator());
        }

        UserDetailResp receiverDetail = userModelResult.getResult();
        receiverList.add(receiverDetail.getUserCode());
        if (TaskType.SKILL.getCode().equals(taskType)) {
            DemeterSkillTask demeterSkillTask = demeterSkillTaskDao.selectByPrimaryKey(taskId);

            ModelResult<UserDetailResp> publisherModelResult = ehrServiceClient.getUserInfo(demeterSkillTask.getPublisher());
            if (!publisherModelResult.isSuccess()) {
                throw new BusinessException("ehr查询用户信息失败，uid = " + demeterSkillTask.getPublisher());
            }

            UserDetailResp publisherModel = publisherModelResult.getResult();
            publisherList.add(publisherModel.getUserCode());
            content1 = receiverDetail.getUserName() + "发起技能点认证，《" + demeterSkillTask.getTaskName() + "》，技能点ID：" + TaskIdPrefix.SKILL_PREFIX.getDesc() + demeterSkillTask.getId() + "，请前往demeter-ui.kp.ziroom.com查看详情。";
            content2 = "您已发起技能点认证，《" + demeterSkillTask.getTaskName() + "》，技能点ID：" + TaskIdPrefix.SKILL_PREFIX.getDesc() + demeterSkillTask.getId() + "，请等待发布者审核。";
        } else if (TaskType.ASSIGN.getCode().equals(taskType)) {
            DemeterAssignTask demeterAssignTask = demeterAssignTaskDao.selectByPrimaryKey(taskId);

            ModelResult<UserDetailResp> publisherModelResult = ehrServiceClient.getUserInfo(demeterAssignTask.getPublisher());
            if (!publisherModelResult.isSuccess()) {
                throw new BusinessException("ehr查询用户信息失败，uid = " + demeterAssignTask.getPublisher());
            }
            UserDetailResp publisher = publisherModelResult.getResult();
            publisherList.add(publisher.getUserCode());
            content1 = receiverDetail.getUserName() + "发起任务验收，《" + demeterAssignTask.getTaskName() + "》，任务ID：" + TaskIdPrefix.ASSIGN_PREFIX.getDesc() + demeterAssignTask.getId() + "，请前往demeter-ui.kp.ziroom.com查看详情。";
            content2 = "您已发起任务验收，《" + demeterAssignTask.getTaskName() + "》，任务ID：" + TaskIdPrefix.ASSIGN_PREFIX.getDesc() + demeterAssignTask.getId() + "，请等待发布者审核。";
        }

        ConsoleMessageModel consoleMessageModel = new ConsoleMessageModel();
        consoleMessageModel.setToUser(publisherList);
        consoleMessageModel.setContext(content1);

        ConsoleMessageModel consoleMessageModel2 = new ConsoleMessageModel();
        consoleMessageModel2.setToUser(receiverList);
        consoleMessageModel2.setContext(content2);

        this.sendMsg(consoleMessageModel);
        this.sendMsg(consoleMessageModel2);
        return false;
    }

    @Override
    public boolean checkoutResultNotice(Long taskId, Integer taskType, String receiverId, Integer result) {
        String content = null;
        List<String> receiverList = new ArrayList<>();

        ModelResult<UserDetailResp> receiverModelResult = ehrServiceClient.getUserInfo(receiverId);
        if (!receiverModelResult.isSuccess()) {
            throw new BusinessException("ehr查询用户信息失败，uid = " + receiverId);
        }
        UserDetailResp receiverDetail = receiverModelResult.getResult();
        receiverList.add(receiverDetail.getUserCode());

        if (TaskType.SKILL.getCode().equals(taskType)) {
            DemeterSkillTask demeterSkillTask = demeterSkillTaskDao.selectByPrimaryKey(taskId);
            content = "您的技能点《" + demeterSkillTask.getTaskName() + "》认证" + CheckoutResult.getByCode(result).getDesc() + "，技能点ID：" + TaskIdPrefix.SKILL_PREFIX.getDesc() + demeterSkillTask.getId() + "，请前往demeter-ui.kp.ziroom.com查看详情。";
        } else if (TaskType.ASSIGN.getCode().equals(taskType)) {
            DemeterAssignTask demeterAssignTask = demeterAssignTaskDao.selectByPrimaryKey(taskId);
            content = "您的任务《" + demeterAssignTask.getTaskName() + "》" + CheckoutResult.getByCode(result).getDesc() + "，任务ID：" + TaskIdPrefix.ASSIGN_PREFIX.getDesc() + demeterAssignTask.getId() + "，请前往demeter-ui.kp.ziroom.com查看详情。";
        }

        ConsoleMessageModel consoleMessageModel = new ConsoleMessageModel();
        consoleMessageModel.setToUser(receiverList);
        consoleMessageModel.setContext(content);
        this.sendMsg(consoleMessageModel);
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

    private void sendMsg(ConsoleMessageModel consoleMessageModel){
        ConsoleMessageChannelEvent consoleMessageChannelEvent = new ConsoleMessageChannelEvent();
        consoleMessageChannelEvent.setData(JSON.toJSONString(consoleMessageModel));
        eventPublisher.publishEvent(consoleMessageChannelEvent);
    }
}
