package com.ziroom.tech.demeterapi.po.dto.resp.task;

import com.ziroom.tech.demeterapi.dao.entity.DemeterTaskUser;
import com.ziroom.tech.demeterapi.dao.entity.TaskFinishConditionInfo;
import com.ziroom.tech.demeterapi.dao.entity.TaskFinishOutcome;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author daijiankun
 */
@Data
public class TaskProgressResp {

    private String receiverName;

    private List<TaskFinishConditionInfoResp> taskFinishConditionInfoRespList;

    private List<TaskFinishOutcome> taskFinishOutcomeList;

    private String checkoutResult;

    private String checkoutOpinion;

    private Date checkoutTime;
}
