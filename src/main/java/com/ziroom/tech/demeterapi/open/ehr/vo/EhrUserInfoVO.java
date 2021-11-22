package com.ziroom.tech.demeterapi.open.ehr.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xuzeyu
 */
@Data
@NoArgsConstructor
public class EhrUserInfoVO {
    /**
     * 姓名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 学历
     */
    private String education;

    /**
     * 入职天数
     */
    private Integer hireDays;

    /**
     * 职位
     */
    private String job;

    /**
     * 职级
     */
    private String position;

    /**
     * 个人熟练技能 以技能点方式展示 最多展示5个
     */
    private String skills;
}
