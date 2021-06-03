package com.ziroom.tech.demeterapi.po.dto.resp.portrait;

import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class EngineeringMetricResp {

    private Integer insertions = 0;

    private Integer deletions = 0;

    private Integer devEquivalent = 0;

    private Integer commitCount = 0;
}
