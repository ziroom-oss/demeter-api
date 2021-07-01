package com.ziroom.tech.demeterapi.common.exception;


import com.ziroom.tech.demeterapi.common.enums.ErrorCode;

/**
 * 业务异常
 *
 * @author huangqiaowei
 * @date 2019-09-23 19:03
 **/
public class BusinessException extends RuntimeException {

    private Integer code;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getDesc());
        this.code = errorCode.getCode();
    }

    public BusinessException(Throwable e, ErrorCode errorCode) {
        super(errorCode.getDesc(), e);
        this.code = errorCode.getCode();
    }

    public BusinessException(Throwable e) {
        super(e);
    }

}
