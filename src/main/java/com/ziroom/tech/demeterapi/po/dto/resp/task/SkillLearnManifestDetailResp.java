package com.ziroom.tech.demeterapi.po.dto.resp.task;

import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillLearnPath;
import com.ziroom.tech.demeterapi.po.dto.req.task.SkillTaskUserExtend;
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
    private List<SkillTaskUserExtend> demeterSkillTasks;
    private List<DemeterSkillLearnPath> demeterSkillLearnPaths;
}
