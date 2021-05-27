package com.ziroom.tech.demeterapi.po.dto.req.portrayal;

import lombok.Data;

import java.util.Date;

/**
 * 日常任务统计
 * @author daijiankun
 */
@Data
public class DailyTaskReq {

    private Date startTime;

    private Date endTime;
}
