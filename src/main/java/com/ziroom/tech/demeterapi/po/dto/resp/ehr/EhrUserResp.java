package com.ziroom.tech.demeterapi.po.dto.resp.ehr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenx34
 * @date 2020/9/27 14:46
 */
@Data
@ApiModel("EHR用户信息")
public class EhrUserResp {

    @ApiModelProperty("用户系统号")
    private String userCode;

    @ApiModelProperty("用户姓名")
    private String name;

}
