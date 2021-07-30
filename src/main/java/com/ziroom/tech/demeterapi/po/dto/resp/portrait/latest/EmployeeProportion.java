package com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
@Builder
public class EmployeeProportion {
    /**
     * 开发当量
     */
    private List<NameValue> devEquivalentList;

    /**
     * 代码行数
     */
    private List<NameValue> codeLineList;

    /**
     * 代码提交次数
     */
    private List<NameValue> commitList;

    /**
     * 项目数
     */
    private List<NameValue> projectList;

    /**
     * 功能数
     */
    private List<NameValue> functionList;

    /**
     * 修复bug数
     */
    private List<NameValue> bugFixList;

    /**
     * 开发价值
     */
    private List<NameValue> devValueList;

    /**
     * 行价值密度
     */
    private List<NameValue> lineDensityList;
}
