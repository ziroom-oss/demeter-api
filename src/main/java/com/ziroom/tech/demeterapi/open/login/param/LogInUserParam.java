package com.ziroom.tech.demeterapi.open.login.param;

import lombok.Data;


/**
 * @author: xuzeyu
 */
@Data
public class LogInUserParam {

    /**
     * 用户名
     */
    private String loginName;

    /**
     * 密码
     */
    private String password;

}
