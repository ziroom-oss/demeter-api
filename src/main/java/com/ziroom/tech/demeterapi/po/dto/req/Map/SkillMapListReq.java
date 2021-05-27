package com.ziroom.tech.demeterapi.po.dto.req.Map;

import com.ziroom.tech.demeterapi.common.PageListReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("查询技术图谱")
public class SkillMapListReq extends PageListReq {
    @ApiModelProperty("创建时间-开始时间段")
    private Date createStartTime;

    @ApiModelProperty("创建时间-结束时间段")
    private Date createEndTime;

    @ApiModelProperty("图谱状态")
    private Byte isEnable;

    @ApiModelProperty("职务")
    private Integer jobId;

    @ApiModelProperty("图谱名称")
    private String name;

    @ApiModelProperty("图谱编号")
    private Long id;
}
