package com.ziroom.tech.demeterapi.open.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * web层返回的统一输出对象
 * @author xuzeyu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelResponse<T> {
    private Boolean success = true;
    private String resultMessage;
    private T result;
    private String resultCode;
}

