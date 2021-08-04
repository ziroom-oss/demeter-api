package com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
@Builder
public class DeptTendency {

    private List<String> monthList;

    private List<DeptTendencyItem> devEquivalentTendencyList;

    private List<DeptTendencyItem> codeListTendencyList;

    private List<DeptTendencyItem> commitTendencyList;

    /**
     * 项目数
     */
    private List<DeptTendencyItem> projectTendencyList;

    /**
     * 功能数
     */
    private List<DeptTendencyItem> functionTendencyList;

    /**
     * 修复bug数
     */
    private List<DeptTendencyItem> bugFixTendencyList;

    /**
     * 开发价值
     */
    private List<DeptTendencyItem> devValueTendencyList;

    /**
     * 行价值密度
     */
    private List<DeptTendencyItem> lineDensityTendencyList;

    private List<DeptTendencyItem> publishTendencyList;

    private List<DeptTendencyItem> compileTendencyList;

    private List<DeptTendencyItem> onlineTendencyList;

    private List<DeptTendencyItem> rollbackTendencyList;

    private List<DeptTendencyItem> restartTendencyList;

}
