package com.ziroom.tech.demeterapi.po.dto.resp.portrait;

import lombok.Builder;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
@Builder
public class UserInfo {

    /**
     * 张三
     */
    private String username;

    private String email;

    private String education;

    /**
     * 入职天数 2333
     */
    private Long hireDays;

    /**
     * 职位 Java工程师
     */
    private String job;

    /**
     * 职级 T3
     */
    private String position;

    /**
     * 个人技能: Java初级、数据库中级、TCP/IP高级
     */
    private String skills;
}
