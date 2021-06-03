package com.ziroom.tech.demeterapi.service;

/**
 * @author daijiankun
 */
public interface MessageService {

    /**
     * 成功创建指派类任务，向接收者发出消息
     * @param taskId 任务id
     * @param taskPublisher 任务发布者
     * @param taskReceiver 任务接收者
     * @return
     */
    boolean sendAssignTaskCreated(Long taskId, String taskPublisher, String taskReceiver);

    /**
     * 任务接收者认领任务或技能，向发布者发出消息
     * 包括技能类任务和指派类任务
     * @return boolean
     */
    boolean acceptNotice(Long taskId, Integer taskType, String skillReleaser);

    /**
     * 任务接收者拒绝任务，向发布者发出消息
     * 指派类任务
     * @return boolean
     */
    boolean rejectTaskNotice(Long taskId);

    /**
     * 任务接收者发起验收，向发布者发出消息
     * @return boolean
     */
    boolean startCheckoutNotice(Long taskId, Integer taskType);


    /**
     * 任务验收结果，通过/不通过，向接收者发出消息
     * @return boolean
     */
    boolean checkoutResultNotice(Long taskId, Integer taskType, String receiverId, Integer result);

    /**
     * 点亮某个技能-本部门全体员工
     * @return boolean
     */
    boolean lighteningSkillToStaff();

    /**
     * 点亮某个技能-执行者
     * @return boolean
     */
    boolean lighteningSkillToExecutor();

    /**
     * 点亮某个技能图谱
     * @return
     */
    boolean lighteningGraphToStaff();

    /**
     * 点亮某个技能图谱
     * @return
     */
    boolean lighteningGraphToExecutor();

}
