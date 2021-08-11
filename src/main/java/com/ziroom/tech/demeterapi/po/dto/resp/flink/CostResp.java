package com.ziroom.tech.demeterapi.po.dto.resp.flink;

import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class CostResp {

    private Integer departmentTotal = 0;

    private Integer developmentNumber = 0;

    private Double developerPercentage = 0.0;

    /**
     * 出勤工时
     */
    private Integer workTime = 0;

    /**
     * 开发工时
     */
    private Integer developTime = 0;

    private Object lavel;

    private Object workNum;

}
