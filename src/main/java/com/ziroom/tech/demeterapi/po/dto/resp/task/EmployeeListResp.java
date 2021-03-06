package com.ziroom.tech.demeterapi.po.dto.resp.task;

import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class EmployeeListResp {

    /**
     * 姓名
     */
    private String name;

    /**
     * 职务
     */
    private String jobName;

    /**
     * 成长值
     */
    private Integer growthValue = 0;

    /**
     * 指派类任务完成数
     */
    private Integer assignTaskCount = 0;

    /**
     * 技能值
     */
    private Integer skillValue = 0;

    /**
     * 技能类任务完成数
     */
    private Integer skillTaskCount = 0;

    /**
     * 认证技能数量
     */
    private Integer skillCount = 0;

    /**
     * 认证技能图谱数量
     */
    private Integer graphCount = 0;
}
