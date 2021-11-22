package com.ziroom.tech.demeterapi.open.model;

import com.ziroom.tech.demeterapi.open.enums.ResponseEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * web层响应包装
 * @author xuzeyu
 */
@Data
@NoArgsConstructor
public class ModelResponseUtil<T> implements Serializable {

    /**
     * 响应成功标识
     */
    private Boolean success;

    /**
     * 错误信息提示 success为true时 resultMessage为空
     */
    private String resultMessage;

    /**
     * 响应结果
     */
    private T result;

    /**
     * 响应码
     */
    private String resultCode;

    /**
     * 响应成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ModelResponse<T> ok(T data) {
        ModelResponse<T> modelResponse = new ModelResponse<>();
        modelResponse.setResultCode(ResponseEnum.RESPONSE_SUCCESS_CODE.getCode());
        modelResponse.setResult(data);
        modelResponse.setSuccess(true);
        return modelResponse;
    }

    /**
     * 响应失败
     * @param resultCode
     * @param message
     * @param <T>
     * @return
     */
    public static <T> ModelResponse<T> error(String resultCode, String message) {
        ModelResponse<T> modelResponse = new ModelResponse<>();
        modelResponse.setSuccess(false);
        modelResponse.setResultCode(resultCode);
        modelResponse.setResultMessage(message);
        return modelResponse;
    }

    /**
     * 响应失败
     * @param message
     * @param <T>
     * @return
     */
    public static <T> ModelResponse<T> error(String message) {
        ModelResponse<T> modelResponse = new ModelResponse<>();
        modelResponse.setSuccess(false);
        modelResponse.setResultCode(ResponseEnum.RESPONSE_ERROR_CODE.getCode());
        modelResponse.setResultMessage(message);
        return modelResponse;
    }

}



