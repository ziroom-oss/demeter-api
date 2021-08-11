package com.ziroom.tech.demeterapi.po.dto.req.task;

import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillTask;
import lombok.Data;

@Data
public class SkillTaskUserExtend extends DemeterSkillTask {
    Long taskUserId;
}
