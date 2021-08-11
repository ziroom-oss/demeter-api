package com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * EHR用户信息
 *
 * @author chenx34
 * @date 2020/4/23 14:47
 */
@Data
@ApiModel("EHR用户信息")
public class EhrUserResp {

    @ApiModelProperty("用户系统号")
    private String userCode;

    @ApiModelProperty("用户姓名")
    private String name;

    @ApiModelProperty("邮箱前缀")
    private String adcode;

    private String jobName;

    private String orgDesc;

    private String orgTree;

    private String token;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("城市")
    private String cityCode;
}
