package com.ziroom.tech.demeterapi.open.enums;

/**
 * web层响应枚举
 * @author xuzeyu
 */
public enum ResponseEnum {
    ERROR_CODE("-1", "操作失败"),

    /**
     * 操作成功
     */
    RESPONSE_SUCCESS_CODE("00000", "操作成功"),
    /**
     * 操作失败
     */
    RESPONSE_ERROR_CODE("50000", "操作失败"),

    /**
     * 返回数据为空
     */
    RESPONSE_DATA_EMPTY("50002", "无匹配数据"),

    /**
     * 服务异常，请稍后重试
     */
    RESPONSE_EXCEPTION_CODE("50001", "未查询到匹配记录"),

    /**
     * 服务异常，请稍后重试
     */
    RESPONSE_ISSUETYPE_CODE("50004", "issue type类型未匹配"),

    /**
     * 未知错误
     */
    PARAM_EMPTY_CODE("10000", "参数不能为空"),

    /**
     * 参数格式不正确
     */
    PARAM_PATTERN_ERROR_CODE("10001", "参数格式不正确"),

    /**
     * 参数超出指定范围
     */
    PARAM_RANGE_CODE("10006", "参数超出指定范围"),

    /**
     * 参数超过限制长度
     */
    PARAM_LENGTH_LIMIT_CODE("10005", "参数超过限制长度"),

    /**
     * 未知错误
     */
    PARAM_ERROR_CODE("10007", "参数错误"),

    /**
     * 参数为空
     */
    RESPONSE_CHECKPARAM_CODE("50005", "请求参数检查错误"),

    /**
     * 最大数量不得超过100
     */
    RESPONSE_CHECKPARAMSIZE_CODE("50006", "请求参数长度不得超过100"),

    /**
     * http调用异常
     */
    RESPONSE_HTTPEXCEPTION_CODE("50003", "服务异常，请稍后重试"),


    PARAM_CHECK_COMMON_ERROR("6000", "通用参数检查错误");



    ResponseEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

