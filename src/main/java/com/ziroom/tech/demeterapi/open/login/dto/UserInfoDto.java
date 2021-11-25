package com.ziroom.tech.demeterapi.open.login.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户详情
 *
 * @author xuzeyu
 **/
@Data
@NoArgsConstructor
public class UserInfoDto {

    /**
     * 用户code
     */
    private String userCode;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 职位
     */
    private String job;

    /**
     * 职务 ID
     */
    private String jobId;

    /**
     * 部门
     */
    private String dept;

    /**
     * 部门code
     */
    private String deptCode;

}
