package com.ziroom.tech.demeterapi.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 指派类任务自身状态
 * @author daijiankun
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CheckoutResult {

    UNKNOWN(-1, "-"),

    /**
     * 验收不通过
     */
    FAILED(0, "验收不通过"),

    /**
     * 验收通过
     */
    SUCCESS(1, "验收通过");

    /**
     * 类型码
     */
    private final Integer code;
    /**
     * 类型描述
     */
    private final String desc;

    private static final Map<Integer, CheckoutResult> MAP = Arrays.stream(CheckoutResult.values())
            .collect(Collectors.toMap(CheckoutResult::getCode, Function.identity()));

    /**
     * 根据类型码获取指定的类型枚举
     * @param code 类型码
     * @return 类型枚举
     */
    public static CheckoutResult getByCode(Integer code) {
        return MAP.get(code);
    }

    /**
     * 获取所有的任务类型
     * @return List<SkillTaskStatus>
     */
    public static List<CheckoutResult> getAllTaskType() {
        return Arrays.stream(CheckoutResult.values()).collect(Collectors.toList());
    }

    /**
     * 判断传入的状态码是不是合法的，当没有找到对应的code或者传入的code为null时，返回false
     * @param code 状态码
     * @return 是否合法 合法:true 不合法:false
     */
    public static boolean isValid(Integer code){
        if(Objects.isNull(code)) {
            return false;
        }
        for(CheckoutResult taskType : CheckoutResult.values()) {
            if(Objects.equals(taskType.code, code)) {
                return true;
            }
        }
        return false;
    }
}
