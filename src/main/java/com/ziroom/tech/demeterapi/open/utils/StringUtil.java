package com.ziroom.tech.demeterapi.open.utils;


/**
 * @author lipp3
 * @date 2021/7/5 13:04
 * @Description
 */
public class StringUtil {

    public static boolean isLong(String s){
        try {
            Long.valueOf(s);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    /**
     * 转换为String类型
     * @param obj
     * @return
     */
    public static String toString(Object obj){
        if(obj == null){
            return "";
        }else{
            return obj.toString();
        }
    }

    public static boolean isEmpty(Object str){
        return str == null || "".equals(str);
    }
}
