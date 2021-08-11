package com.ziroom.tech.demeterapi.po.dto.req.email;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author libingsi
 * @date 2021/8/4 17:24
 */
@Data
@ApiModel("员工邮箱信息")
public class UserEmailDto {
    @ApiModelProperty(value = "唯一id")
    private String id;
    @ApiModelProperty(value = "主邮箱")
    private String email;
    @ApiModelProperty(value = "子邮箱")
    private String subEmail;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
}
