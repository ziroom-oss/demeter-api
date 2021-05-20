package com.ziroom.tech.demeterapi.po.dto.resp.task;

import lombok.Data;

import java.util.Date;

/**
 * @author daijiankun
 */
@Data
public class ReceiveQueryResp {

    private Long id;

    private String taskNo;

    private String taskName;

    private Date taskStartTime;

    private Date taskEndTime;

    private Integer taskReward;

    private Integer taskType;

    private String taskTypeName;

    private Integer taskStatus;

    private String taskStatusName;

    private Integer taskFlowStatus;

    private String taskFlowStatusName;

    private String taskDescription;

    private String publisher;

    private String publisherName;

    private String receiver;

    private String receiverName;

    /**
     * 指派类任务是否需要验收
     */
    private Byte needAcceptance;

    private Date createTime;

    private Date updateTime;

    private String createId;

    private String modifyId;
}
