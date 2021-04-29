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
 * 技能任务流状态
 * @author daijiankun
 */

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SkillTaskFlowStatus {

    /**
     * 任务
     */
    FORBIDDEN(0, "已禁用"),
    /**
     * 需求
     */
    UNFORBIDDEN(1, "已启用"),

    ONGOING(2, "进行中"),

    TO_AUTHENTICATE(3, "待认证"),

    PASS(4, "认证通过"),

    FAILED(5, "认证未通过");

    /**
     * 类型码
     */
    private Integer code;
    /**
     * 类型描述
     */
    private String desc;

    private static Map<Integer, SkillTaskFlowStatus> map = Arrays.stream(SkillTaskFlowStatus.values())
            .collect(Collectors.toMap(SkillTaskFlowStatus::getCode, Function.identity()));

    /**
     * 根据类型码获取指定的类型枚举
     * @param code 类型码
     * @return 类型枚举
     */
    public static SkillTaskFlowStatus getByCode(Integer code) {
        return map.get(code);
    }

    /**
     * 获取所有的任务类型
     * @return List<SkillTaskStatus>
     */
    public static List<SkillTaskFlowStatus> getAllTaskType() {
        return Arrays.stream(SkillTaskFlowStatus.values()).collect(Collectors.toList());
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
        for(SkillTaskFlowStatus taskType : SkillTaskFlowStatus.values()) {
            if(Objects.equals(taskType.code, code)) {
                return true;
            }
        }
        return false;
    }
}
