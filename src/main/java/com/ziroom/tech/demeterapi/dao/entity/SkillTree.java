package com.ziroom.tech.demeterapi.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SkillTree {

    private Integer id;

    private String name;

    private Integer parentId;

    private Byte sort;

    private Date createTime;

    private Date modifyTime;

    private Byte isDel;
}