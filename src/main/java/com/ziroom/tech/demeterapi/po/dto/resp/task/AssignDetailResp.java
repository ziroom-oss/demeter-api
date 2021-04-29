package com.ziroom.tech.demeterapi.po.dto.resp.task;

import com.ziroom.tech.demeterapi.dao.entity.TaskFinishCondition;
import com.ziroom.tech.demeterapi.dao.entity.TaskFinishOutcome;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author daijiankun
 */
@Data
public class AssignDetailResp {

    private Long id;

    private String taskName;

    private Byte taskEffectiveImmediate;

    private Date taskStartTime;

    private Date taskEndTime;

    private Integer taskReward;

    private String taskAttachmentUrl;

    private Integer taskStatus;

    private String taskStatusName;

    private String taskTypeName;

    private String taskDescription;

    private String publisher;

    private String publisherName;

    private String receiver;

    private String receiverName;

    private Byte needEmailRemind;

    private Byte needPunishment;

    private Byte needAcceptance;

    private Date createTime;

    private Date updateTime;

    private String createId;

    private String modifyId;

    private List<TaskFinishCondition> taskFinishConditionList;

    private List<TaskFinishConditionInfoResp> taskFinishConditionInfoRespList;

    private List<TaskFinishOutcome> taskFinishOutcomeList;
}
