package com.ziroom.tech.demeterapi.po.dto.resp.map;

import java.util.List;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class SummaryData {

    /**
     * 认证技能点数量
     */
    private Integer authedSkillPointCount;

    /**
     * 认证技能数量
     */
    private Integer authedSkillCount;

    /**
     * 技能图谱认证 ＆ 进度
     */
    private List<SummaryMapResp> skillGraph;
}
