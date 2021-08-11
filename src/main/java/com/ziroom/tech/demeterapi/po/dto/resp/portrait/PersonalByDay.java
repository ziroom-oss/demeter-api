package com.ziroom.tech.demeterapi.po.dto.resp.portrait;

import lombok.Builder;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
@Builder
public class PersonalByDay {

    private String day;

    private Integer devEquivalent;

    private Integer insertions;
}
