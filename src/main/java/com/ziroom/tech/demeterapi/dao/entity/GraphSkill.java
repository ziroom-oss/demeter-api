package com.ziroom.tech.demeterapi.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class GraphSkill {

    private Long id;
    // 技能图谱名称
    private String graphName;
    // 启用状态
    private Integer enable;
    // 职务
    private Integer position;
    private Date createTime;
    private Date modifyTime;
}