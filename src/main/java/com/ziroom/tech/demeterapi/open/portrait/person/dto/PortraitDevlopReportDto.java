package com.ziroom.tech.demeterapi.open.portrait.person.dto;

import lombok.Data;

/**
 * @author xuzeyu
 */
@Data
public class PortraitDevlopReportDto {

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
    private Integer commitCount;

    /**
     * 项目数
     */
    private Integer projectNum;

    /**
     * 处理需求数
     */
    private Integer demandNum;

    /**
     * 处理bug数
     */
    private Integer bugNum;

    /**
     * 开发价值
     */
    private String devValue;

    /**
     * 项目平均开发周期
     */
    private String projectAveDevPeriod;

    /**
     * 功能平均开发周期
     */
    private String functionAveDevPeriod;

    /**
     * bug平均修复时间
     */
    private String bugAveFixTime;

    /**
     * 发布次数
     */
    private Integer publishNum;

    /**
     * 编译次数
     */
    private Integer compileNum;

    /**
     * 上线次数
     */
    private Integer onlineNum;

    /**
     * 重启次数
     */
    private Integer restartNum;

    /**
     * 回滚次数
     */
    private Integer rollbackNum;

    /**
     * 注释覆盖度
     */
    private String docCoverage;

    /**
     * 测试覆盖度
     */
    private String staticTestCoverage;

    /**
     * 代码影响力
     */
    private String funImpact;

    /**
     * 千行bug率
     */
    private String bugProbability;

    /**
     * 出勤工时
     */
    private String workHours;

    /**
     * 开发工时
     */
    private String devlopHours;

    /**
     * 工作饱和度
     */
    private String workSaturability;

    /**
     * 休假天数
     */
    private String vacationDays;


}
