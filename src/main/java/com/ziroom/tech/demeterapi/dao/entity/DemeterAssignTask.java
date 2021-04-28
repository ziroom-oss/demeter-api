package com.ziroom.tech.demeterapi.dao.entity;

import java.util.Date;

/**
 * <pre>
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑, 永无BUG!
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * </pre>
 */
public class DemeterAssignTask {

    private Long id;

    private String taskName;

    private Byte taskEffectiveImmediate;

    private Date taskStartTime;

    private Date taskEndTime;

    private Integer taskReward;

    private String taskAttachmentUrl;

    private Integer taskStatus;

    private String taskDescription;

    private String publisher;

    private Byte needEmailRemind;

    private Byte needPunishment;

    private Byte needAcceptance;

    private Date createTime;

    private Date updateTime;

    private String createId;

    private String modifyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Byte getTaskEffectiveImmediate() {
        return taskEffectiveImmediate;
    }

    public void setTaskEffectiveImmediate(Byte taskEffectiveImmediate) {
        this.taskEffectiveImmediate = taskEffectiveImmediate;
    }

    public Date getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(Date taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public Date getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(Date taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public Integer getTaskReward() {
        return taskReward;
    }

    public void setTaskReward(Integer taskReward) {
        this.taskReward = taskReward;
    }

    public String getTaskAttachmentUrl() {
        return taskAttachmentUrl;
    }

    public void setTaskAttachmentUrl(String taskAttachmentUrl) {
        this.taskAttachmentUrl = taskAttachmentUrl;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Byte getNeedEmailRemind() {
        return needEmailRemind;
    }

    public void setNeedEmailRemind(Byte needEmailRemind) {
        this.needEmailRemind = needEmailRemind;
    }

    public Byte getNeedPunishment() {
        return needPunishment;
    }

    public void setNeedPunishment(Byte needPunishment) {
        this.needPunishment = needPunishment;
    }

    public Byte getNeedAcceptance() {
        return needAcceptance;
    }

    public void setNeedAcceptance(Byte needAcceptance) {
        this.needAcceptance = needAcceptance;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }
}