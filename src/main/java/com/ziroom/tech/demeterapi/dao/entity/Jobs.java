package com.ziroom.tech.demeterapi.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Jobs {

    private Long id;
    /**
     * 职务代号与公司同步
     */
    private Integer code;
    /**
     * 职务名称
     */
    private String name;

    private Date createTime;

    private Date modifyTime;

    private Byte isDel;
}