package com.ziroom.tech.demeterapi.open.portrait.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xuzeyu
 */
@Data
@NoArgsConstructor
public class ChartLegendModel {
    /**
     * 环比上升 or 下降
     * increase or decrease
     */
    private String type;

    /**
     * 变化比例
     */
    private String value;
}
