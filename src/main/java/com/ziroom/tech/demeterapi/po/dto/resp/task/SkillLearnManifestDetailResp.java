package com.ziroom.tech.demeterapi.po.dto.resp.task;

import com.ziroom.tech.demeterapi.core.SkillTree;
import lombok.Data;

/**
 * @author lipp3
 * @date 2021/7/1 9:23
 * @Description
 */
@Data
public class SkillLearnManifestDetailResp extends SkillLearnManifestResp {
    private SkillTree skillTree;
}
