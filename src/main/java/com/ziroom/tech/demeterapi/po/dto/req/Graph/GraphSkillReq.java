package com.ziroom.tech.demeterapi.po.dto.req.Graph;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("新增和编辑技能图谱")
public class GraphSkillReq {
    @ApiModelProperty("主键id，也是图谱编号")
    private Integer id;

    @ApiModelProperty("图谱名称")
    private String graphName;

    @ApiModelProperty("启用状态")
    private Byte enable;

    @ApiModelProperty("职务")
    private Integer position;
}
