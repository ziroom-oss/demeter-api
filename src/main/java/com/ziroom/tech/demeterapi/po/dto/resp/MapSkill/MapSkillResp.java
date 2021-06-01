package com.ziroom.tech.demeterapi.po.dto.resp.MapSkill;

import com.ziroom.tech.demeterapi.po.dto.resp.task.ReleaseQueryResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询的技能图谱和技能点关联列表")
public class MapSkillResp {
    @ApiModelProperty("图谱和技能点的关联表主键")
    private Long id;

    @ApiModelProperty("技能点")
    private ReleaseQueryResp releaseQueryResp;
}
