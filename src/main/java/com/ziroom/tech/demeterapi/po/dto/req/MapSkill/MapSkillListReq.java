package com.ziroom.tech.demeterapi.po.dto.req.MapSkill;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Api("查询技能图谱-技能点记录")
@Data
public class MapSkillListReq {
    @ApiModelProperty("图谱 id")
    private Integer skillMapId;

    @ApiModelProperty("技能点 id")
    private Long skillTaskId;
}
