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
 * 当前登录人角色
 * @author daijiankun
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CurrentRole {

    /**
     * 普通用户
     */
    PLAIN("plain", "普通用户"),

    /**
     * 部门管理员
     */
    DEPT("demeter-dept-admin", "部门管理员"),

    /**
     * 超级管理员
     */
    SUPER("demeter-super-admin", "超级管理员");

    /**
     * 角色码
     */
    private String code;
    /**
     * 角色描述
     */
    private String desc;

    private static Map<String, CurrentRole> map = Arrays.stream(CurrentRole.values())
            .collect(Collectors.toMap(CurrentRole::getCode, Function.identity()));

    /**
     * 根据类型码获取指定的类型枚举
     * @param code 类型码
     * @return 类型枚举
     */
    public static CurrentRole getByCode(String code) {
        return map.get(code);
    }

    /**
     * 获取所有的任务类型
     * @return List<SkillTaskStatus>
     */
    public static List<CurrentRole> getAllTaskType() {
        return Arrays.stream(CurrentRole.values()).collect(Collectors.toList());
    }

    /**
     * 判断传入的状态码是不是合法的，当没有找到对应的code或者传入的code为null时，返回false
     * @param code 状态码
     * @return 是否合法 合法:true 不合法:false
     */
    public static boolean isValid(String code){
        if(Objects.isNull(code)) {
            return false;
        }
        for(CurrentRole taskType : CurrentRole.values()) {
            if(Objects.equals(taskType.code, code)) {
                return true;
            }
        }
        return false;
    }
}
