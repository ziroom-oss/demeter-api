package com.ziroom.tech.demeterapi.common;

import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.zcloud.sso.ZCloudUserInfo;

import java.util.Optional;

/**
 * 获取当前登陆人
 *
 * @author xuzeyu
 **/
public class OperatorContext {

    private static final ThreadLocal<UserDetailResp> userInfoThreadLocal = new ThreadLocal<>();

    public static void setOperator() {
        Optional<ZCloudUserInfo> currentUser = ZCloudUserInfo.current();
        currentUser.ifPresent(zCloudUserInfo -> {
            UserDetailResp userDetailResp = new UserDetailResp();
            userDetailResp.setUserCode(zCloudUserInfo.getUid());
            userDetailResp.setUserName(zCloudUserInfo.getUserName());
            userDetailResp.setDeptCode("102558");
            userInfoThreadLocal.set(userDetailResp);
        });
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
