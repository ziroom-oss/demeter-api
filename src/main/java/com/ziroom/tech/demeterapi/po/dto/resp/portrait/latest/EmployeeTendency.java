package com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
@Builder
public class EmployeeTendency {

    private List<String> monthList;

    private List<EmployeeTendencyItem> devEquivalentTendencyList;

    private List<EmployeeTendencyItem> codeListTendencyList;

    private List<EmployeeTendencyItem> commitTendencyList;

    /**
     * 项目数
     */
    private List<EmployeeTendencyItem> projectTendencyList;

    /**
     * 功能数
     */
    private List<EmployeeTendencyItem> functionTendencyList;

    /**
     * 修复bug数
     */
    private List<EmployeeTendencyItem> bugFixTendencyList;

    /**
     * 开发价值
     */
    private List<EmployeeTendencyItem> devValueTendencyList;

    /**
     * 行价值密度
     */
    private List<EmployeeTendencyItem> lineDensityTendencyList;

    private List<EmployeeTendencyItem> publishTendencyList;

    private List<EmployeeTendencyItem> compileTendencyList;

    private List<EmployeeTendencyItem> onlineTendencyList;

    private List<EmployeeTendencyItem> rollbackTendencyList;

    private List<EmployeeTendencyItem> restartTendencyList;
}
