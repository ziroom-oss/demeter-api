package com.ziroom.tech.demeterapi.common;

/**
 * @author xuzeyu
 */
public class UserParamThreadLocal {

    private static final ThreadLocal<UserParam> userHeaderParamThreadLocal = new ThreadLocal<>();

    private UserParamThreadLocal() {

    }

    public static void set(UserParam userParam) {
        userHeaderParamThreadLocal.set(userParam);
    }

    public static UserParam get() {
        return userHeaderParamThreadLocal.get();
    }

    public static void remove() {
        userHeaderParamThreadLocal.remove();
    }

}
