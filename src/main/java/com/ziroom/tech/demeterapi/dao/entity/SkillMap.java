package com.ziroom.tech.demeterapi.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SkillMap {

    private Long id;

    private String name;

    private Byte isEnable;
    /**
     * 关联到 jobs 表
     */
    private Integer jobId;

    private Date createTime;

    private Date modifyTime;

    private Byte isDel;
}