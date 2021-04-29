package com.ziroom.tech.demeterapi.po.dto.resp.task;

import lombok.Data;

import java.util.Date;

@Data
public class TaskFinishConditionInfoResp {

    private Long id;

    private String uid;

    private Long taskId;

    private String taskFinishContent;

    private Integer taskConditionStatus;

    private String taskConditionStatusName;

    private Date createTime;

    private Date modifyTime;

    private String createId;

    private String modifyId;
}