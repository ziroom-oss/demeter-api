package com.ziroom.tech.demeterapi.po.dto.resp.ehr;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户详情
 *
 * @author huangqiaowei
 * @date 2020-07-01 19:41
 **/
@Data
@Api("用户详情")
public class UserDetailResp {

    private String userCode;

    private String userName;

    @ApiModelProperty("职务 P:主职")
    private String job;

    @ApiModelProperty("职务 ID")
    private String jobId;

    @ApiModelProperty("部门")
    private String dept;

    @ApiModelProperty("部门code")
    private String deptCode;

    @ApiModelProperty("组")
    private String group;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    private String highestEducation;

    private String levelName;

    private String photo;

    /**
     * 转正日期？
     */
    private String effdt;

    private String treePath;
}
