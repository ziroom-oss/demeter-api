package com.ziroom.tech.demeterapi.common.utils;

import lombok.experimental.UtilityClass;

/**
 * Created by liangrk on 2020/8/27.
 */
@UtilityClass
public class UserUtils {

    public static String mailToCode(String mail) {
        return mail.substring(0, mail.indexOf('@'));
    }

    public static String codeToMail(String code) {
        return code + "@ziroom.com";
    }

}
