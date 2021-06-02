package com.ziroom.tech.demeterapi.po.dto.req.MapSkill;

import com.google.common.base.Preconditions;
import com.ziroom.tech.demeterapi.dao.entity.SkillMapSkill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

/**
 * @author daijinru
 */
@Data
@ApiModel("修改图谱和技能点关联记录")
public class MapSkillModReq {
    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("职级")
    private Byte jobLevel;

    public SkillMapSkill getEntity(MapSkillModReq mapSkillModReq) {
        SkillMapSkill skillMapSkill = new SkillMapSkill();
        validate();
        BeanUtils.copyProperties(mapSkillModReq, skillMapSkill);
        return skillMapSkill;
    }

    public void validate() {
        Preconditions.checkArgument(Objects.nonNull(id), "主键 id 不能为空");
    }
}
