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
 * 支配类任务状态
 * @author daijiankun
 */

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AssignTaskStatus {

    /**
     * 待认领
     */
    UNCLAIMED(0, "待认领"),

    /**
     * 已拒绝
     */
    FORBIDDEN(1, "已拒绝"),

    /**
     * 进行中
     */
    ONGOING(2, "进行中"),

    /**
     * 已完成
     */
    FINISHED(3, "已完成"),

    /**
     * 未完成
     */
    UNFINISHED(4, "未完成"),

    /**
     * 验收通过
     */
    ACCEPTANCE(5, "验收通过"),

    /**
     * 验收未通过
     */
    FAILED(6, "验收未通过");

    /**
     * 类型码
     */
    private Integer code;
    /**
     * 类型描述
     */
    private String desc;

    private static Map<Integer, AssignTaskStatus> map = Arrays.stream(AssignTaskStatus.values())
            .collect(Collectors.toMap(AssignTaskStatus::getCode, Function.identity()));

    /**
     * 根据类型码获取指定的类型枚举
     * @param code 类型码
     * @return 类型枚举
     */
    public static AssignTaskStatus getByCode(Integer code) {
        return map.get(code);
    }

    /**
     * 获取所有的任务类型
     * @return List<SkillTaskStatus>
     */
    public static List<AssignTaskStatus> getAllTaskType() {
        return Arrays.stream(AssignTaskStatus.values()).collect(Collectors.toList());
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
        for(AssignTaskStatus taskType : AssignTaskStatus.values()) {
            if(Objects.equals(taskType.code, code)) {
                return true;
            }
        }
        return false;
    }
}
