package com.ziroom.tech.demeterapi.po.dto.req.task;

import lombok.Data;

import java.util.Date;

/**
 * @author daijiankun
 */
@Data
public class CreateAssignReq {

    private String taskName;

    private Byte taskEffectiveImmediate;

    private Date taskStartTime;

    private Date taskEndTime;

    private String taskReceiver;

    private Integer taskReward;

    private String taskAttachmentUrl;

    private String taskDescription;
}
