package com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
@Builder
public class TeamOverviewResp {

    private String style;

    private Integer id;

    private String name;

    private List<Metric> qoqMetricList;

    private List<Metric> yoyMetricList;
}
