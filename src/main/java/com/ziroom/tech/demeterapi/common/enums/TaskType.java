package com.ziroom.tech.demeterapi.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author chenx34
 * @date 2020/8/13 15:38
 *
 * 任务类型
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TaskType {

    /**
     * 任务
     */
    TASK((byte) 0, "任务"),
    /**
     * 需求
     */
    DEMAND((byte) 1, "需求"),
    /**
     * 缺陷修复
     */
    BUG_FIX((byte) 2, "缺陷修复");

    /**
     * 类型码
     */
    private byte code;
    /**
     * 类型描述
     */
    private String desc;

    private static Map<Byte, TaskType> map = Arrays.stream(TaskType.values())
            .collect(Collectors.toMap(TaskType::getCode, Function.identity()));

    /**
     * 根据类型码获取指定的类型枚举
     * @param code 类型码
     * @return 类型枚举
     */
    public static TaskType getByCode(byte code) {
        return map.get(code);
    }

    /**
     * 获取所有的任务类型
     * @return List<TaskType>
     */
    public static List<TaskType> getAllTaskType() {
        // FIXME 初期仅提供任务类型
        return Collections.singletonList(TaskType.TASK);
//        return Arrays.stream(TaskType.values()).collect(Collectors.toList());
    }

    /**
     * 判断传入的状态码是不是合法的，当没有找到对应的code或者传入的code为null时，返回false
     * @param code 状态码
     * @return 是否合法 合法:true 不合法:false
     */
    public static boolean isValid(Byte code){
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
