package com.ziroom.tech.demeterapi.open.portrait.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 提交记录分析报告
 * @author xuzeyu
 */
@Data
@NoArgsConstructor
public class CommitReportEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 提交时间
     */
    private String commitTime;

    /**
     * commit hash
     */
    private String commitId;

    /**
     * 作者
     */
    private String contributor;

    /**
     * 作者邮箱
     */
    private String contributorEmail;

    /**
     * gitlab项目所属gitlab组id
     */
    private String groupCode;

    /**
     * gitlab项目所属组name
     */
    private String groupName;

    /**
     * 项目code
     */
    private String projectCode;

    /**
     * 项目名
     */
    private String projectName;

    /**
     * 开发当量
     */
    private Long devEquivalent;

    /**
     * 新增行数
     */
    private Long insertions;

    /**
     * 删除行数
     */
    private Long deletions;

    /**
     * 部门code
     */
    private String departmentCode;

    /**
     * 部门名字
     */
    private String departmentName;

    /**
     * 开发价值
     */
    private String devlopValue;

    /**
     * 价值密度
     */
    private String devlopValueDensity;

    /**
     * 注释函数数目
     */
    private String hasDoc;

    /**
     * 被测试覆盖函数数目
     */
    private String hasTestCoverage;

    /**
     * 提交影响的函数数目
     */
    private String funImpact;

    /**
     * 提交函数总数目
     */
    private String commitFunTotal;

    /**
     * 函数总数目
     */
    private String funTotal;

    /**
     * 技能点
     */
    private String skilPoints;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String moditiyTime;
}
