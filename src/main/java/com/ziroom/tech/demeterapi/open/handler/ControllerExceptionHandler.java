package com.ziroom.tech.demeterapi.open.handler;

import com.ziroom.tech.demeterapi.common.exception.BusinessException;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * 统一异常捕获器
 *
 * @author xuzeyu
 **/
@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Resp handleParamValidate(BusinessException e) {
        log.error(String.format("system is error , message is {}", e.getMessage()), e);
        return Resp.error(e.getMessage());
    }

    /**
     * 参数错误
     */
    @ExceptionHandler(value = {IllegalArgumentException.class, HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.OK)
    public Resp handleParamValidate(Exception e) {
        log.error(String.format("system is error , message is {}", e.getMessage()), e);
        return Resp.error(e.getMessage());
    }

    /**
     * 参数错误
     */
    @ExceptionHandler(value = {SQLException.class, DataAccessException.class})
    @ResponseStatus(HttpStatus.OK)
    public Resp handleParamValidate(SQLException e) {
        log.error(String.format("system is error , message is {}", e.getMessage()), e);
        return Resp.error("db error");
    }

    /**
     * 其他所有异常  <br>
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Resp handleOthers(Exception e) {
        log.error(String.format("system is error , message is {}", e.getMessage()), e);
        return Resp.error(e.getMessage());
    }
}
