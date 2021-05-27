package com.ziroom.tech.demeterapi.po.dto.resp.portrait;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author daijiankun
 */
@Data
@Builder
public class GrowthInfo {

    /**
     * 成长值
     */
    private Integer growthValue;

    private List<Date> growthValueDate;
    private List<Integer> growthValueAcc;

    /**
     * 指派类任务完成数量
     */
    private Integer assignTaskCount;

    /**
     * 技能值
     */
    private Integer skillValue;

    /**
     * 技能类任务完成数量
     */
    private Integer skillTaskCount;

    /**
     * 认证技能数量
     */
    private Integer skillAuthCount;

    /**
     * 认证技能图谱数量
     */
    private Integer skillGraphCount;

}
