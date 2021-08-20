package com.ziroom.tech.demeterapi.po.dto.resp.task;

import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillLearnPath;
import com.ziroom.tech.demeterapi.dao.entity.DemeterUserLearnManifest;
import com.ziroom.tech.demeterapi.dao.entity.TaskFinishCondition;
import com.ziroom.tech.demeterapi.po.vo.LearnManifestVo;
import lombok.Data;

import java.util.List;

/**
 * @author daijiankun
 */
@Data
public class TaskDetailResp {

    private AssignDetailResp assignDetailResp;

    private SkillDetailResp skillDetailResp;

    private List<ReceiverListResp> receiverList;


//    private Object graphInfo;

    private List<LearnManifestVo> learnManifest;

    private List<DemeterSkillLearnPath> demeterSkillLearnPathList;

    private List<TaskFinishCondition> taskFinishConditionList;

    private List<TaskFinishConditionInfoResp> taskFinishConditionInfoRespList;

    private List<TaskFinishOutcomeResp> taskFinishOutcomeRespList;
}
