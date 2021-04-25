package com.ziroom.tech.demeterapi.common;

/**
 * 获取当前登陆人
 *
 * @author huangqiaowei
 * @date 2019-09-27 16:54
 **/
public class OperatorContext {

    private static final ThreadLocal<String> operator = new ThreadLocal<>();

    public static void setOperator(String operatorId) {
        operator.set(operatorId);
    }

    public static String getOperator() {
        return operator.get();
    }

}
