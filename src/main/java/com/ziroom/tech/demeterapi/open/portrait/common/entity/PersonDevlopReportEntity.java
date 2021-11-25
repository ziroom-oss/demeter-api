package com.ziroom.tech.demeterapi.open.portrait.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 开发者分析报告
 * @author xuzeyu
 */
@Data
@NoArgsConstructor
public class PersonDevlopReportEntity {
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
     * 职级
     */
    private String level;

    /**
     * 开发当量
     */
    private Long devEquivalent;

    /**
     * 新增代码行数
     */
    private Long insertions;

    /**
     * 删除代码行数
     */
    private Long deletions;

    /**
     * 代码提交次数
     */
    private Long commitNum;

    /**
     * 项目数
     */
    private Long projectNum;

    /**
     * 处理需求数
     */
    private Long demandNum;

    /**
     * 处理bug数
     */
    private Long bugNum;

    /**
     * 发布次数
     */
    private Long publishNum;

    /**
     * 编译次数
     */
    private Long compileNum;

    /**
     * 上线次数
     */
    private Long onlineNum;

    /**
     * 重启次数
     */
    private Long restartNum;

    /**
     * 回滚次数
     */
    private Long rollbackNum;

    /**
     * 出勤工时
     */
    private Long workHours;

    /**
     * 开发工时
     */
    private Long devlopHours;

    /**
     * 工作饱和度
     */
    private String workSaturability;

    /**
     * 休假天数
     */
    private String vacationDays;

    /**
     * 部门code
     */
    private String departmentCode;

    /**
     * 部门
     */
    private String departmentName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String moditiyTime;
}
