package com.ziroom.tech.demeterapi.po.dto.resp.task;

import com.ziroom.tech.demeterapi.core.SkillTree;
import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillLearnPath;
import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillTask;
import lombok.Data;

import java.util.List;

/**
 * @author lipp3  modified by zhangxt3
 * @date 2021/7/1 9:23
 * @Description
 */
@Data
public class SkillLearnManifestDetailResp extends SkillLearnManifestResp {

    //private SkillTree skillTree;
    private List<DemeterSkillTask> demeterSkillTasks;
    private List<DemeterSkillLearnPath> demeterSkillLearnPaths;
}
