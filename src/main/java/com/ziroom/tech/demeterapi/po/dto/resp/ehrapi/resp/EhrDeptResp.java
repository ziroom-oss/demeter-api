package com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * EHR部门
 *
 * @author chenx34
 * @date 2020/4/23 14:19
 */
@Data
@ApiModel("EHR部门")
public class EhrDeptResp {

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty(value = "部门编码", notes = "这里的部门编码是新的部门编码")
    private String code;
}
