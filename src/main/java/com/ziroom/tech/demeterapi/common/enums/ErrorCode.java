package com.ziroom.tech.demeterapi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author donghao
 * @version 1.0
 * 2019/3/6.
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * 网络请求服务器响应失败
     */
    NET_REQUEST_ERROR(500001,"网络请求服务器响应失败"),
    /**
     * 网络请求超时或者请求失败
     */
    NET_REQUEST_SUSPEND(500002,"网络请求超时或者请求失败");
    /**
     * 状态码
     */
    private int code;
    /**
     * 状态描述
     */
    private String desc;

}
