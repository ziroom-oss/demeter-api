package com.ziroom.tech.demeterapi.po.dto.resp.flink;

import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class CostResp {

    private Integer departmentTotal;

    private Integer developmentNumber;

    private Double developerPercentage;

    /**
     * 出勤工时
     */
    private Integer workTime;

    /**
     * 开发工时
     */
    private Integer developTime;

    private Object lavel;

    private Object workNum;

}
