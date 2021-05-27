package com.ziroom.tech.demeterapi.po.dto.resp.portrait;

import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class ReleaseMetricsResp {

    /**
     * 进行中
     */
    private Long ongoing;

    /**
     * 已关闭
     */
    private Long closed;

    /**
     * 已完成
     */
    private Long completed;

}
