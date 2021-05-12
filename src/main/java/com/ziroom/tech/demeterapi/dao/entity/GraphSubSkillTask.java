package com.ziroom.tech.demeterapi.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class GraphSubSkillTask {

    private Long id;
    // 关联的技能ID
    private Integer skillId;
    // 关联的任务ID
    private String taskId;
    // 技能等级 1 初级 2 中级 3 高级
    private Integer level;
    // 子技能名称
    private String name;
    // 职称
    private Integer jobLevel;
    private Date createTime;
    private Date modifyTime;
    private String createId;
    private String modifyId;
}