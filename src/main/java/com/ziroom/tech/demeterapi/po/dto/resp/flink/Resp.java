package com.ziroom.tech.demeterapi.po.dto.resp.flink;

import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class Resp<T> {

    private Integer code;

    private String message;

    private T data;
}
