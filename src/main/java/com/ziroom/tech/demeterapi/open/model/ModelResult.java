package com.ziroom.tech.demeterapi.open.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 业务层通用返回
 * @author xuzeyu
 */
@Data
@NoArgsConstructor
public class ModelResult<T> {
    private Boolean success;
    private String resultMessage;
    private T result;
    private String resultCode;

    public boolean isSuccess() {
        return success;
    }
}
