package com.ziroom.tech.demeterapi.po.dto.resp.portrait;

import lombok.Builder;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
@Builder
public class DevStruct {

    private String name;

    private Integer devEquivalent;

    private Integer insertions;
}
