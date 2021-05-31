package com.ziroom.tech.demeterapi.po.dto.req.MapSkill;

import com.google.common.base.Preconditions;
import com.ziroom.tech.demeterapi.dao.entity.SkillMapSkill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

@Data
@ApiModel("创建图谱和技能点关联")
public class MapSkillCreateReq {
    @ApiModelProperty("图谱编号 id")
    private Integer skillMapId;

    @ApiModelProperty("技能点 id")
    private Long skillTaskId;

    public SkillMapSkill getEntity() {
        SkillMapSkill skillMapSkill = new SkillMapSkill();
        validate();
        skillMapSkill.setSkillMapId(skillMapId);
        skillMapSkill.setSkillTaskId(skillTaskId);
        return skillMapSkill;
    }

    public void validate() {
        Preconditions.checkArgument(Objects.nonNull(skillMapId), "图谱 id 不能为空");
        Preconditions.checkArgument(Objects.nonNull(skillTaskId), "技能点 id 不能为空");
    }
}
