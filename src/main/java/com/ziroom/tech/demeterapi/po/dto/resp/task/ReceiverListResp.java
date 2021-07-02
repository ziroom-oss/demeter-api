package com.ziroom.tech.demeterapi.po.dto.resp.task;

import lombok.Data;

import java.util.Date;

/**
 * @author daijiankun
 */
@Data
public class ReceiverListResp {

    private Long id;

    private Long taskId;

    private Integer taskType;

    private String receiverUid;

    /**
     * 接收人姓名
     */
    private String receiverName;

    /**
     * 所属部门
     */
    private String receiverDept;

    private Integer taskStatus;

    /**
     * 任务执行状态描述
     */
    private String taskStatusName;

    private Integer checkResult;

    private String checkOpinion;

    private Date createTime;

    private Date modifyTime;

    private String createId;

    private String modifyId;
}
