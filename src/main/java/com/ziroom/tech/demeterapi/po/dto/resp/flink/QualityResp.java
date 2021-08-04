package com.ziroom.tech.demeterapi.po.dto.resp.flink;

import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class QualityResp {

    private String departmentCode;

    private Double docCoverage = 0.0;

    private Double staticTestCoverage = 0.0;

    private Double modularity = 0.0;

}
