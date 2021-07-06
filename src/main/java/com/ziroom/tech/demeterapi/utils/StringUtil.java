package com.ziroom.tech.demeterapi.utils;

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
}
