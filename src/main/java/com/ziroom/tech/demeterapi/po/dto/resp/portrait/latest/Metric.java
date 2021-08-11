package com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest;

import lombok.Builder;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
@Builder
public class Metric {

    private String name;

    private String value;

    private String oldValue;

    /**
     * 0 不变，1 上升，2 下降
     */
    private Integer tendency = 0;

    private String rate;
}
