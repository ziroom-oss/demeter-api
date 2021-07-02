package com.ziroom.tech.demeterapi.po.dto.req.portrayal;

import lombok.Data;

import java.util.Date;

/**
 * @author daijiankun
 */
@Data
public class EngineeringMetricReq {

    private String uid;

    private Date startTime;

    private Date endTime;
}
