package com.ziroom.tech.demeterapi.po.dto.resp.portrait;

import lombok.Builder;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
@Builder
public class DevOverviewStruct {

    private Integer insertions;

    private Integer deletions;

    private Integer devEquivalent;

    private Integer commitCounts;
}
