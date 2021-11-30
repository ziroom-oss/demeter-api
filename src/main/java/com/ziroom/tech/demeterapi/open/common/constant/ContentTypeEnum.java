package com.ziroom.tech.demeterapi.open.common.constant;

/**
 * @author: xuzeyu
 */
public enum ContentTypeEnum {

    TEXT_HTML("text/html"),
    FORM("application/x-www-form-urlencoded"),
    MULTIPART_FORM("multipart/form-data"),
    APPLICATION_JSON("application/json"),
    ;
    public String value;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

      ContentTypeEnum(String value) {
        this.value = value;
    }
}
