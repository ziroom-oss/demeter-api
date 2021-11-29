package com.ziroom.tech.demeterapi.po.dto.req.ehr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author daijiankun
 * @date 8/14/2020
 * @description
 */
@Data
@ApiModel("获取员工列表")
public class EhrEmpListReq {

    @ApiModelProperty("姓名")
    private String empCodeNameAdcode;

    @ApiModelProperty("部门编号")
    private String orgCode;

}