package com.ziroom.tech.demeterapi.po.dto.resp.map;

import com.ziroom.tech.demeterapi.dao.entity.SkillMap;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class SummaryMapResp {

    private Double progress;

    private SkillMap skillMap;
}
