package com.ziroom.tech.demeterapi.po.dto.resp.portrait;

import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class ReceiveMetricsResp {

    /**
     * 待认领
     */
    private Long unclaimed;

    /**
     * 已拒绝
     */
    private Long rejected;

    /**
     * 进行中
     */
    private Long ongoing;

    /**
     * 已完成
     */
    private Long finished;

    /**
     * 已延期
     */
    private Long unfinished;

    /**
     * 待验收
     */
    private Long waitingAccept;

    /**
     * 验收通过
     */
    private Long acceptance;

    /**
     * 验收未通过
     */
    private Long failed;
}
