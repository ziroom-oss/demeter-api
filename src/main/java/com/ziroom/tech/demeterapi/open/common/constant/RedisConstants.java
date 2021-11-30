package com.ziroom.tech.demeterapi.open.common.constant;

/**
 * @author: xuzeyu
 */
public class RedisConstants {
    public static class KEY {
        public static final String DEMETER_FRONT_LOGIN_STRING = "demeter:front:login:";

        public static final String DEMETER_FRONT_LOGIN_MOBILE_STRING = "demeter:front:mLoginValCode:";

        public static final String DEMETER_FRONT_APP_SECRET_MOBILE_STRING = "demeter:front:mAppSeVaCode:";

        public static final String DEMETER_FRONT_FORGET_PASSWORD_STRING = "demeter:front:mForgetPasswVaCode:";

        public static final String DEMETER_FRONT_RESET_APP_SECRET_STRING = "demeter:front:mResetAppSeVaCode:";

        public static final String DEMETER_FRONT_MODIFY_PASSWORD_STRING = "demeter:front:mModifyPasswVaCode:";

        public static final String DEMETER_FRONT_MENU_STRING = "demeter:front:menu:";

        public static final String DEMETER_FRONT_MENU_DOC_STRING = DEMETER_FRONT_MENU_STRING + "doc:";

        public static final String DEMETER_FRONT_INTERFACE_DOC_STRING ="demeter:front:interface:doc:";

        public static final String DEMETER_FRONT_ARTICLE_CONTENT_STRING ="demeter:front:article:";

    }

    public static final long DEMETER_FRONT_LOGIN_EXPIRE = 45 * 60;
    public static final long DEMETER_FRONT_LOGIN_RENEW_EXPIRE = 15 * 60;
    public static final long DEMETER_FRONT_MOBILE_VALIDATE_CODE_EXPIRE = 30 * 60;
    public static final long DEMETER_FRONT_APP_SECRET_MOBILE_EXPIRE = 30 * 60;
    public static final long DEMETER_FRONT_FORGET_PASSWORD_EXPIRE = 30 * 60;
    public static final long DEMETER_FRONT_RESET_APP_SECRET_EXPIRE = 30 * 60;
    public static final long DEMETER_FRONT_MODIFY_PASSWORD_EXPIRE = 30 * 60;
    public static final long DEMETER_FRONT_MENU_EXPIRE = 2 * 60;
    public static final long DEMETER_FRONT_INTERFACE_EXPIRE = 2 * 60;
    public static final long DEMETER_FRONT_ARTICLE_EXPIRE = 2 * 60;
}
