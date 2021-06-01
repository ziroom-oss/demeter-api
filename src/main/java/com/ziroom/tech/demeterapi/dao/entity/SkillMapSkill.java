package com.ziroom.tech.demeterapi.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SkillMapSkill {

    private Long id;

    private Integer skillMapId;

    private Long skillTaskId;

    private Byte isDel;

    private Date createTime;

    private Date modifyTime;
}