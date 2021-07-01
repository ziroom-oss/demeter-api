package com.ziroom.tech.demeterapi.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 任务类型
 * @author daijiankun
 */

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TaskType {

    /**
     * 全部
     */
    ALL(0, "全部"),

    /**
     * 技能类任务
     */
    SKILL(1, "技能类任务"),
    /**
     * 指派类任务
     */
    ASSIGN(2, "指派类任务");

    /**
     * 类型码
     */
    private Integer code;
    /**
     * 类型描述
     */
    private String desc;

    private static Map<Integer, TaskType> map = Arrays.stream(TaskType.values())
            .collect(Collectors.toMap(TaskType::getCode, Function.identity()));

    /**
     * 根据类型码获取指定的类型枚举
     * @param code 类型码
     * @return 类型枚举
     */
    public static TaskType getByCode(Integer code) {
        return map.get(code);
    }

    /**
     * 获取所有的任务类型
     * @return List<TaskType>
     */
    public static List<TaskType> getAllTaskType() {
        return Arrays.stream(TaskType.values()).collect(Collectors.toList());
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
        for(TaskType taskType : TaskType.values()) {
            if(Objects.equals(taskType.code, code)) {
                return true;
            }
        }
        return false;
    }
}
