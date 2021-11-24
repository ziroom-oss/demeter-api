package com.ziroom.tech.demeterapi.common;

import com.ziroom.zcloud.sso.ZCloudUserInfo;

import java.util.Optional;

/**
 * 获取当前登陆人
 *
 * @author xuzeyu
 **/
public class OperatorContext {

    private static final ThreadLocal<String> operator = new ThreadLocal<>();

    public static void setOperator() {
        Optional<ZCloudUserInfo> currentUser = ZCloudUserInfo.current();
        currentUser.ifPresent(zCloudUserInfo -> operator.set(zCloudUserInfo.getUid()));
    }

    public static String getOperator() {
        return operator.get();
    }

}
