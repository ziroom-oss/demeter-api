package com.ziroom.tech.demeterapi.open.auth.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色
 * @author xuzeyu
 */
@Data
@NoArgsConstructor
public class AuthUserEntity {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户code
     */
    private String userCode;

    /**
     * 角色
     */
    private String role;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String moditiyTime;
}