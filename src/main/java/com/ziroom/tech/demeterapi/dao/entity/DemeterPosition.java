package com.ziroom.tech.demeterapi.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DemeterPosition {

    private Long id;
    // 职务编号
    private Integer code;
    // 职务名称
    private String name;
    private Date createTime;
    private Date modifyTime;
}