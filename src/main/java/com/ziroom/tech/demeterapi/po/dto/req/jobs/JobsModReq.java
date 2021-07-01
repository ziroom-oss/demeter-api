package com.ziroom.tech.demeterapi.po.dto.req.jobs;

import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

@Data
@ApiModel("变更职务")
public class JobsModReq extends JobsCreateReq{
    @ApiModelProperty("职务ID")
    private Long id;

    @Override
    public void validate() {
        Preconditions.checkArgument(Objects.nonNull(id), "职务 id 不能为空");
    }
}
