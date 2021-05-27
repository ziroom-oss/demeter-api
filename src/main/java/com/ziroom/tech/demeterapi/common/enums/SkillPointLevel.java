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
 * 技能点等级
 * @author daijiankun
 */

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SkillPointLevel {

    /**
     * 全部
     */
    ALL(0, "全部"),

    /**
     * 初级
     */
    BEGINNER(1, "初级"),

    /**
     * 中级
     */
    INTERMEDIATE(2, "中级"),

    /**
     * 高级
     */
    ADVANCE(3, "高级");

    private Integer code;

    private String desc;

    private static Map<Integer, SkillPointLevel> map = Arrays.stream(SkillPointLevel.values())
            .collect(Collectors.toMap(SkillPointLevel::getCode, Function.identity()));

    /**
     * 根据类型码获取指定的类型枚举
     * @param code 类型码
     * @return 类型枚举
     */
    public static SkillPointLevel getByCode(Integer code) {
        return map.get(code);
    }

    /**
     * 获取所有的任务类型
     * @return List<SkillTaskStatus>
     */
    public static List<SkillPointLevel> getAllSkillLevel() {
        return Arrays.stream(SkillPointLevel.values()).collect(Collectors.toList());
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
        for(SkillPointLevel taskType : SkillPointLevel.values()) {
            if(Objects.equals(taskType.code, code)) {
                return true;
            }
        }
        return false;
    }
}
