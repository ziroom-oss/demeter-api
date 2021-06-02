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
public class SkillDetailResp {

    private Long id;

    private String taskNo;

    private String taskName;

    private Integer taskStatus;

    private String taskStatusName;

    private Integer skillReward;

    private Integer skillTreeId;

    private Integer skillLevel;

    private String skillLevelName;

    private String attachmentUrl;

    private String attachmentName;

    private String publisher;

    private String publisherName;

    private List<String> taskReceiver;

    private String receiverName;

    private String taskRemark;

    private Integer taskType;

    private String taskTypeName;

    private Date createTime;

    private Date modifyTime;

    private String createId;

    private String modifyId;

    private List<TaskFinishCondition> taskFinishConditionList;

    private List<TaskFinishConditionInfoResp> taskFinishConditionInfoRespList;

    private List<TaskFinishOutcome> taskFinishOutcomeList;
}
