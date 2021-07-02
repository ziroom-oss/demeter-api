package com.ziroom.tech.demeterapi.po.dto.resp.task;

import com.ziroom.tech.demeterapi.dao.entity.TaskFinishCondition;
import com.ziroom.tech.demeterapi.dao.entity.TaskFinishOutcome;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author daijiankun
 */
@Data
public class AssignDetailResp {

    private Long id;

    private String taskNo;

    private String taskName;

    private Date taskStartTime;

    private Date taskEndTime;

    private Integer taskReward;

    private String attachmentUrl;

    private String attachmentName;

    private Integer taskStatus;

    private String taskStatusName;

    private Integer taskType;

    private String taskTypeName;

    private String taskDescription;

    private String publisher;

    private String publisherName;

    private List<String> taskReceiver;

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
