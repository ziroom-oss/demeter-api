package com.ziroom.tech.demeterapi.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 城市编码
 *
 * @author hddchange
 */
@AllArgsConstructor
@Getter
public enum CityCodeEnum {

    bj("110000", "北京"),
    tj("120000", "天津"),
    sh("310000", "上海"),
    sz("440300", "深圳"),
    hz("330100", "杭州"),
    nj("320100", "南京"),
    cd("510100", "成都"),
    gz("440100", "广州"),
    wy("420100", "武汉");

    private String code;

    private String name;

    private static final Map<String, CityCodeEnum> MAP;

    static {
        MAP = Arrays.stream(CityCodeEnum.values()).collect(Collectors.toMap(CityCodeEnum::getCode,
                Function.identity()));
    }

    public static CityCodeEnum getByCode(String code) {
        return Optional.ofNullable(MAP.get(code)).orElse(bj);
    }

}
