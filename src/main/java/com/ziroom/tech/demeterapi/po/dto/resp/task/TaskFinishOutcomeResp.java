package com.ziroom.tech.demeterapi.po.dto.resp.task;

import lombok.Data;

import java.util.Date;

/**
 * @author daijiankun
 */
@Data
public class TaskFinishOutcomeResp {

    private Long id;

    private String fileAddress;

    private String fileName;

    private Long taskId;

    private Integer taskType;

    private String receiverUid;

    private Date createTime;

    private Date modifyTime;

    private String createId;

    private String modifyId;
}
