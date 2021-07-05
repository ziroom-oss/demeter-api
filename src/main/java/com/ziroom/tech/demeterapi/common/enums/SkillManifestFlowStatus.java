package com.ziroom.tech.demeterapi.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lipp3
 * @date 2021/6/30 18:19
 * @Description
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum  SkillManifestFlowStatus {

    ONGOING(1, "进行中"),

    PASS(2, "认证通过");

    /**
     * 类型码
     */
    private Integer code;
    /**
     * 类型描述
     */
    private String name;
}
