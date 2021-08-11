package com.ziroom.tech.demeterapi.po.dto.resp.flink;

import lombok.Builder;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
@Builder
public class QualityReq {

    private String startTime;

    private String endTime;

    private String departmentCode;
}
