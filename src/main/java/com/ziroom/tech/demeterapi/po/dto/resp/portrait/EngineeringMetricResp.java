package com.ziroom.tech.demeterapi.po.dto.resp.portrait;

import lombok.Data;

import java.util.Date;

/**
 * @author daijiankun
 */
@Data
public class EngineeringMetricResp {

    private Integer insertions;

    private Integer deletions;

    private Integer devEquivalent;

    private Integer commitCount;
}
