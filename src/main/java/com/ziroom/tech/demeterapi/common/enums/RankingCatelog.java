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
 * 页面排名模块
 * @author zhangxintong
 */

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RankingCatelog {


    ASPNUM(0, "认证技能点数量"),

    ASNUM(1, "认证技能数量"),

    HSP(3, "热门技能点"),

    HS(4, "热门技能"),

    DEQUIV(5, "开发当量"),

    DVALUE(6, "开发价值"),

    DQUALITY(7, "开发质量"),

    DEFFI(8, "开发效率");




    private Integer code;
    private String title;

    private static Map<Integer, RankingCatelog> map = Arrays.stream(RankingCatelog.values())
            .collect(Collectors.toMap(RankingCatelog::getCode, Function.identity()));

    /**
     * 根据类型码获取指定的类型枚举
     * @param code 类型码
     * @return 类型枚举
     */
    public static RankingCatelog getByCode(Integer code) {
        return map.get(code);
    }

    /**
     * 获取所有的任务类型
     * @return List<SkillTaskStatus>
     */
    public static List<RankingCatelog> getAllRankingCatelog() {
        return Arrays.stream(RankingCatelog.values()).collect(Collectors.toList());
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
        for(RankingCatelog rankingCatelog : RankingCatelog.values()) {
            if(Objects.equals(rankingCatelog.code, code)) {
                return true;
            }
        }
        return false;
    }
}
