package com.ziroom.tech.demeterapi.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@JsonFormat
public enum TechRanks {
    T1(1, "T1"),
    T2(2, "T2"),
    T3(3, "T3"),
    T4(4, "T4"),
    T5(5, "T5"),
    T6(6, "T6"),
    T7(7, "T7"),
    T8(8, "T8");

    private final Integer code;
    private final String desc;

    private static final Map<Integer, TechRanks> MAP = Arrays.stream(TechRanks.values())
            .collect(Collectors.toMap(TechRanks::getCode, Function.identity()));

    public static List<Map<Integer, TechRanks>> listAllTechRanks() {
        List<Map<Integer, TechRanks>> list = new ArrayList<Map<Integer, TechRanks>>();
        for (TechRanks tr : TechRanks.values()) {
            Map<Integer, TechRanks> map = new HashMap<>();
            map.put(tr.getCode(), tr);
            list.add(map);
        }
        return list;
    }

    public static TechRanks getByCode(Integer code) {
        return MAP.get(code);
    }
}
