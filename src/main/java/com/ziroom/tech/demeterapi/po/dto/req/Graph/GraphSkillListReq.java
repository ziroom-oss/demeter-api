package com.ziroom.tech.demeterapi.po.dto.req.Graph;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("查询技能图谱列表")
public class GraphSkillListReq {
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("图谱状态")
    private Byte enable;

    @ApiModelProperty("职务")
    private Integer position;

    @ApiModelProperty("图谱名称")
    private String graphName;

    @ApiModelProperty("图谱编号")
    private Long id;
}
