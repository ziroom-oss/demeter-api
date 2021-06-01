package com.ziroom.tech.demeterapi.po.dto.req.MapSkill;

import com.google.common.base.Preconditions;
import com.ziroom.tech.demeterapi.dao.entity.SkillMapSkill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

@Data
@ApiModel("创建图谱和技能点关联")
public class MapSkillCreateReq {
    @ApiModelProperty("图谱编号 id")
    private Integer skillMapId;

    @ApiModelProperty("技能点 id")
    private Long skillTaskId;

    @ApiModelProperty("职级")
    private Byte jobLevel;

    public SkillMapSkill getEntity(MapSkillCreateReq mapSkillCreateReq) {
        SkillMapSkill skillMapSkill = new SkillMapSkill();
        validate();
        BeanUtils.copyProperties(mapSkillCreateReq, skillMapSkill);
        return skillMapSkill;
    }

    public void validate() {
        Preconditions.checkArgument(Objects.nonNull(skillMapId), "图谱 id 不能为空");
        Preconditions.checkArgument(Objects.nonNull(skillTaskId), "技能点 id 不能为空");
    }
}
