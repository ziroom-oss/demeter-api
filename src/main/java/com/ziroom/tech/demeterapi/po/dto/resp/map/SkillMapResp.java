package com.ziroom.tech.demeterapi.po.dto.resp.map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Optional;

@Data
@ApiModel("查询的技能图谱列表")
public class SkillMapResp {
    @ApiModelProperty("图谱编号")
    private Long id;
    @ApiModelProperty("图谱名称")
    private String name;
    @ApiModelProperty("适用职务")
    private Optional<String> jobName;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("最后更新时间")
    private Date modifyTime;
    @ApiModelProperty("启用状态")
    private Byte isEnable;
}
