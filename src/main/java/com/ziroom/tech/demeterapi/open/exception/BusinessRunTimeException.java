package com.ziroom.tech.demeterapi.open.exception;

/**
 * 业务异常类
 * @author xuzeyu
 */
public class BusinessRunTimeException extends RuntimeException{

    private String code;
    private String errorMessage;

    public BusinessRunTimeException(Throwable e) {
        super(e);
    }

    public BusinessRunTimeException(String errorMessage) {
        super(errorMessage);
    }

    public BusinessRunTimeException(String code, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public String getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}