package com.ziroom.tech.demeterapi.po.dto.resp.task;

import lombok.Data;

import java.util.Date;

/**
 * @author daijiankun
 */
@Data
public class TaskFinishConditionInfoResp {

    private Long id;

    private String uid;

    private Long taskId;

    private Integer taskType;

    private Long taskFinishConditionId;

    private String taskFinishContent;

    private Integer taskConditionStatus;

    /**
     * 任务条件状态名
     */
    private String taskConditionStatusName;

    private Date createTime;

    private Date modifyTime;

    private String createId;

    private String modifyId;
}