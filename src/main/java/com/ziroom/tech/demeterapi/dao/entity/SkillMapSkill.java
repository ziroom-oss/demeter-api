package com.ziroom.tech.demeterapi.dao.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author daijinru
 */
@Data
public class SkillMapSkill {

    private Long id;

    private Integer skillMapId;

    private Long skillTaskId;

    private Byte isDel;

    private Byte jobLevel;

    private Date createTime;

    private Date modifyTime;
}