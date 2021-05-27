package com.ziroom.tech.demeterapi.po.dto.resp.portrait;

import lombok.Data;

/**
 * 日常任务统计
 * @author daijiankun
 */
@Data
public class DailyTaskResp {

    /**
     * 全部
     */
    private ReceiveMetricsResp all;

    private Integer allCount;

    /**
     * 接收的
     */
    private ReceiveMetricsResp received;

    private Integer receivedCount;


    /**
     * 发布的
     */
    private ReleaseMetricsResp released;

    private Integer releasedCount;
}
