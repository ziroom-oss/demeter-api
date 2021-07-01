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

    @ApiModelProperty("姓名、邮箱前缀、系统号")
    private String empCodeNameAdcode;

    @ApiModelProperty("电话号")
    private String phone;

    @ApiModelProperty("部门编号")
    private String orgCode;

    @ApiModelProperty("部门名称")
    private String orgName;

    @ApiModelProperty("职务名称")
    private String jobName;

    @ApiModelProperty("职务序列")
    private String seriesCode;

    @ApiModelProperty("页数")
    private Integer page;

    @ApiModelProperty("每页记录数")
    private Integer size;
}