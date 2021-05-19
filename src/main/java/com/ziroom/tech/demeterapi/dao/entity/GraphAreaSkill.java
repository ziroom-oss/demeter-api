package com.ziroom.tech.demeterapi.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class GraphAreaSkill {

    private Long id;
    // 关联的图谱ID
    private Integer graphId;
    // 技能领域名称
    private String skillAreaName;
    // 技能名称
    private String skillName;
    private Date createTime;
    private Date modifyTime;
}