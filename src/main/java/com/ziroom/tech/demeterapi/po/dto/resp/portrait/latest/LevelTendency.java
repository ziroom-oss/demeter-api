package com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
@Builder
public class LevelTendency {

    private List<String> monthList;

    private List<LevelTendencyItem> levelTendencyItemList;

    private List<LevelTendencyItem> devEquivalentTendencyList;

    private List<LevelTendencyItem> codeListTendencyList;

    private List<LevelTendencyItem> commitTendencyList;

    /**
     * 项目数
     */
    private List<LevelTendencyItem> projectTendencyList;

    /**
     * 功能数
     */
    private List<LevelTendencyItem> functionTendencyList;

    /**
     * 修复bug数
     */
    private List<LevelTendencyItem> bugFixTendencyList;

    /**
     * 开发价值
     */
    private List<LevelTendencyItem> devValueTendencyList;

    /**
     * 行价值密度
     */
    private List<LevelTendencyItem> lineDensityTendencyList;

    private List<LevelTendencyItem> publishTendencyList;

    private List<LevelTendencyItem> compileTendencyList;

    private List<LevelTendencyItem> onlineTendencyList;

    private List<LevelTendencyItem> rollbackTendencyList;

    private List<LevelTendencyItem> restartTendencyList;
}
