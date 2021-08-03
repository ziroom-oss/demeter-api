package com.ziroom.tech.demeterapi.po.dto.resp.flink;

import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class StabilityResp {

    private String statisticTime;

    private String departmentCode;

    private Double robustness;

    private Double stability;

    private String updateTime;
}
