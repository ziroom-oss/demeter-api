package com.ziroom.tech.demeterapi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chenx34
 * @date 2020/8/13 20:08
 *
 * 删除状态枚举
 */
@Getter
@AllArgsConstructor
public enum IsDel {

    /**
     * 未删除
     */
    NOT_DELETED((byte) 0, "未删除"),
    /**
     * 已删除
     */
    DELETED((byte) 1, "已删除");

    /**
     * 状态码
     */
    private byte code;
    /**
     * 状态描述
     */
    private String desc;
}
