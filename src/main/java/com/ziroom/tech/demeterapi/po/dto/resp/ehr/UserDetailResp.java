package com.ziroom.tech.demeterapi.po.dto.resp.ehr;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息
 * @author xuzeyu
 **/
@Data
@Api("用户详情")
@NoArgsConstructor
public class UserDetailResp {

    /**
     * 用户登录账号
     */
    private String loginCode;

    /**
     * 用户登录密码
     */
    private String password;

    /**
     * 用户code 唯一标识
     */
    private String userCode;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 职务
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

    /**
     * 部门路径
     */
    private String treePath;

    /**
     * 组
     */
    private String group;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 最高学历
     */
    private String highestEducation;

    /**
     * 职级
     */
    private String levelName;

    /**
     * 照片地址
     */
    private String photo;

    public UserDetailResp(String userCode, String userName, String email, String dept, String deptCode, String treePath) {
        this.userCode = userCode;
        this.userName = userName;
        this.dept = dept;
        this.deptCode = deptCode;
        this.email = email;
        this.treePath = treePath;
    }
}
