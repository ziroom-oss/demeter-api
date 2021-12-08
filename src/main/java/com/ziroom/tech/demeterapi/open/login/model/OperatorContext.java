package com.ziroom.tech.demeterapi.open.login.model;

import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;

/**
 * 获取当前登陆人
 *
 * @author xuzeyu
 **/
public class OperatorContext {

    private static final ThreadLocal<UserDetailResp> userInfoThreadLocal = new ThreadLocal<>();

    public static void setOperator(UserDetailResp userDetailResp) {
        userInfoThreadLocal.set(userDetailResp);
    }

    public static String getOperator() {
        return userInfoThreadLocal.get().getUserCode();
    }

    public static UserDetailResp get() {
        return userInfoThreadLocal.get();
    }

    public static void remove() {
        userInfoThreadLocal.remove();
    }

}
