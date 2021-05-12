package com.ziroom.tech.demeterapi.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class GraphSkill {

    private Long id;
    // 图谱名称
    private String graphName;
    // 启用状态
    private Integer enable;
    // 职务名称
    private String position;
    private Date createTime;
    private Date modifyTime;
    private String createId;
    private String modifyId;
}