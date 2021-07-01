package com.ziroom.tech.demeterapi.po.dto.resp.Tree;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询技能树节点")
public class SkillTreeResp {
    @ApiModelProperty("父节点 id")
    private Integer parentId;

    @ApiModelProperty("节点 id")
    private Integer id;

    @ApiModelProperty("节点名称")
    private String name;

    @ApiModelProperty("节点排序")
    private Byte sort;

}
