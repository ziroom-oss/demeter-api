package com.ziroom.tech.demeterapi.po.dto.resp.MapSkill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询的技能图谱和技能点关联列表")
public class MapSkillResp {
    @ApiModelProperty("图谱和技能点的关联表主键")
    private Long id;

    @ApiModelProperty("技能点 id")
    private Long skillTaskId;

    @ApiModelProperty("图谱 id")
    private Integer skillMapId;

    @ApiModelProperty("职级")
    private Byte jobLevel;

    @ApiModelProperty("技能等级")
    private Integer skillLevel;

    @ApiModelProperty("技能名称")
    private String skillName;

    @ApiModelProperty("技能奖励")
    private Integer skillReward;

    @ApiModelProperty("发布人")
    private String publisher;

    @ApiModelProperty("记录中的技能所属的树节点id，该值返回前端以后组合数组用于批量查询技能点")
    private Integer skillTreeId;
}
