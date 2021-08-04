package com.ziroom.tech.demeterapi.po.dto.resp.flink;

import lombok.Builder;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
@Builder
public class PersonOverview {

    // 产出类
    /**
     * 开发当量
     */
    private Long devEquivalent;

    /**
     * 代码行数
     */
    private String codeLine;

    /**
     * 代码提交次数
     */
    private Long commitCount;

    /**
     * 项目数/功能数/修复bug数
     */
    private String summaryData;

    /**
     * 开发价值
     */
    private String devValue;

    /**
     * 价值密度
     */
    private String valueDensity;


    // 效率类
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


    // 发布类

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


    // 质量类

    /**
     * 注释覆盖度
     */
    private Double docCoverage = 0.0;

    /**
     * 测试覆盖度
     */
    private Double staticTestCoverage = 0.0;

    /**
     * 代码模块性
     */
    private Double modularity = 0.0;

    /**
     * 千行bug率
     */
    private String bugProbability;

    // 成本类

    /**
     * 出勤工时/开发工时/工时饱和度
     */
    private String costSummaryData;

    /**
     * 休假天数
     */
    private String vacationDays;

}
