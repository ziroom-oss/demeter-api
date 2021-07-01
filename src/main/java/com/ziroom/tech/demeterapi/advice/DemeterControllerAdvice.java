package com.ziroom.tech.demeterapi.advice;

import com.ziroom.tech.demeterapi.common.exception.BusinessException;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;
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
 * @author huangqiaowei
 * @date 2019-11-01 10:22
 **/
@RestControllerAdvice
@Slf4j
public class DemeterControllerAdvice {
    /**
     * 业务异常
     *
     * @param exception 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Resp handleParamValidate(BusinessException exception) {
        log.error(exception.getMessage(), exception);
        return Resp.error(exception.getMessage());
    }

    /**
     * 参数错误
     *
     * @param exception 参数验证错误
     */
    @ExceptionHandler(value = {IllegalArgumentException.class, HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.OK)
    public Resp handleParamValidate(Exception exception) {
        log.error(exception.getMessage(), exception);
        return Resp.error(exception.getMessage());
    }

    /**
     * 参数错误
     *
     * @param exception 参数验证错误
     */
    @ExceptionHandler(value = {SQLException.class, DataAccessException.class})
    @ResponseStatus(HttpStatus.OK)
    public Resp handleParamValidate(SQLException exception) {
        log.error(exception.getMessage(), exception);
        return Resp.error("db error");
    }

    /**
     * 其他所有异常  <br>
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Resp handleOthers(Exception exception) {
        log.error(exception.getMessage(), exception);
        return Resp.error(exception.getMessage());
    }
}
