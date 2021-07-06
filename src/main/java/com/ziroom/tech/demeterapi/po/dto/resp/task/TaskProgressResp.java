package com.ziroom.tech.demeterapi.po.dto.resp.task;

import com.ziroom.tech.demeterapi.dao.entity.DemeterAuthHistory;
import com.ziroom.tech.demeterapi.dao.entity.TaskFinishCondition;
import com.ziroom.tech.demeterapi.dao.entity.TaskFinishOutcome;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author daijiankun
 */
@Data
public class TaskProgressResp {

    private Long id;

    private Long taskId;

    private Integer taskType;

    private String receiverUid;

    private String receiverName;

    private List<TaskFinishCondition> taskFinishConditionList;

    private List<TaskFinishConditionInfoResp> taskFinishConditionInfoRespList;

    private List<TaskFinishOutcome> taskFinishOutcomeList;

    private String checkoutResult;

    private String checkoutOpinion;

    private Date checkoutTime;

    private List<AuthHistoryResp> taskAuthHistoryList;
}
