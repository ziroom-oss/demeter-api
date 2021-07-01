package com.ziroom.tech.demeterapi.po.dto.resp.ehr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chenx34
 * @date 2020/6/24 11:36
 */
@Data
@ApiModel("EHR部门")
public class EhrDeptResp implements Serializable {

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty(value = "部门编码", notes = "这里的部门编码是新的部门编码")
    private String code;
}
