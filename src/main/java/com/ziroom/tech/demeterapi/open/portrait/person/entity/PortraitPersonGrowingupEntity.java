package com.ziroom.tech.demeterapi.open.portrait.person.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 开发者个人成长信息实体
 * @author xuzeyu
 */
@Data
@NoArgsConstructor
public class PortraitPersonGrowingupEntity {
    /**
     * 主键
     */
    private Long id;

    /**
     * 作者
     */
    private String contributor;

    /**
     * 作者邮箱
     */
    private String contributorEmail;

    /**
     * 部门code
     */
    private String departmentCode;

    /**
     * 部门名字
     */
    private String departmentName;

    /**
     * 标题
     */
    private String title;

    /**
     * 指标名称
     */
    private String coreName;

    /**
     * 指标值
     */
    private String coreData;

    /**
     * 样式类型
     */
    private Integer type;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String moditiyTime;
}