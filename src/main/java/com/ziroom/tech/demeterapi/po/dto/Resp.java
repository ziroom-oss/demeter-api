package com.ziroom.tech.demeterapi.po.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 响应结果封装
 *
 * @author fangcheng
 * @date 2018/12/11
 */
@Setter
@Getter
@ApiModel
public class Resp<T> implements Serializable {
    /**
     * 默认成功编号
     */
    private static final String CODE_SUCCESS = "200";
    /**
     * 默认成功信息
     */
    private static final String MESSAGE_SUCCESS = "success";
    /**
     * 默认错误编号
     */
    private static final String CODE_ERROR = "40008";
    /**
     * 默认错误信息
     */
    private static final String MESSAGE_ERROR = "error";

    /**
     * 状态码
     */
    @ApiModelProperty(notes = "响应结果编号，成功为200")
    private String code;
    /**
     * 状态信息
     */
    @ApiModelProperty(notes = "响应结果信息")
    private String message;

    /**
     * 数据对象
     */
    @ApiModelProperty(notes = "响应结果数据")
    private T data;

    public Resp() {
    }

    public Resp(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Resp<T> success(T value) {
        return new Resp<>(CODE_SUCCESS, MESSAGE_SUCCESS, value);
    }

    public static <T> Resp<T> success() {
        return new Resp<>(CODE_SUCCESS, MESSAGE_SUCCESS, null);
    }

    public static <T> Resp<T> error(String errorCode, T data) {
        return new Resp<>(errorCode, MESSAGE_ERROR, data);
    }

    public static <T> Resp<T> error(String errorCode, String errorMsg) {
        return new Resp<>(errorCode, errorMsg, null);
    }

    public static <T> Resp<T> error(String errorMsg) {
        return new Resp<>(CODE_ERROR, errorMsg, null);
    }

    public static <T> Resp<T> error() {
        return new Resp<>(CODE_ERROR, null, null);
    }
}
