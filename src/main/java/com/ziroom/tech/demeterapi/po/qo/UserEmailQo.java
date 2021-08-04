package com.ziroom.tech.demeterapi.po.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author libingsi
 * @date 2021/8/4 17:24
 */
@Data
@ApiModel("员工邮箱信息")
public class UserEmailQo {
    @ApiModelProperty(value = "主邮箱")
    private String email;
    @ApiModelProperty(value = "子邮箱")
    private String subEmail;
}
