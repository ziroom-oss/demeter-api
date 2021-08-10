package com.ziroom.tech.demeterapi.po.dto.resp.flink;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class AnalysisResp {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date statisticTime;

    /**
     * 部门code
     */
    private String departmentCode;

    /**
     * 部门名字
     */
    private String departmentName;

    /**
     * 邮箱前缀
     */
    private String uid;
    /**
     * 工号
     */
    private String workNum;
    /**
     * 项目名
     */
    private String projectName;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 职级
     */
    private String level;

    /*****Omega***/

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
     * 回滚次数
     */
    private Integer rollbackNum;

    /**
     * 重启次数
     */
    private Integer restartNum;

    /*****Merico***/

    /**
     * commit数
     */
    private Integer commitCount;

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
     * 技能点
     */
    private String skilPoints;

    /**
     * 开发价值
     */
    private Double developmentValue = 0.0;

    /**
     * 价值密度
     */
    private Double valueDensity = 0.0;

    /*****Jira***/
    /**
     * 处理需求数
     */
    private Integer processDemandCount;

    /**
     * 解决bug数
     */
    private Integer fixBugCount;

    /**
     * 完成项目数
     */
    private Integer completeProjectCount;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
}
