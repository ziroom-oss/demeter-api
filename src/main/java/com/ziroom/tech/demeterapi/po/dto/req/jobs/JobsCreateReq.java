package com.ziroom.tech.demeterapi.po.dto.req.jobs;

import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

@Data
@ApiModel("创建职务")
public class JobsCreateReq {
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("职务编号")
    private Integer code;

    @ApiModelProperty("职务名称")
    private String name;

    public void validate() {
        Preconditions.checkArgument(Objects.nonNull(code), "职务编号不能为空");
        Preconditions.checkArgument(code > 0, "职务编号不能为 0");
        Preconditions.checkArgument(Objects.nonNull(name), "职务名称不能为空");
    }
}
