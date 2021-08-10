package com.ziroom.tech.demeterapi.po.dto.resp.ehr;


import com.ziroom.tech.demeterapi.dao.entity.Jobs;
import io.swagger.annotations.Api;
import lombok.Data;

@Data
@Api("用户详情")
public class UserDetailBySimpleResp {

    private String adCode;

    private String email;

    private String empcode;

    private String empName;

    private String empType;

    private Jobs jobs;

    private String phone;

    private String sex;

    private int status;

}
