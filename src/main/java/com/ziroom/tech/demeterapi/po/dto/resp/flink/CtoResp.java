package com.ziroom.tech.demeterapi.po.dto.resp.flink;

import com.ziroom.tech.demeterapi.po.dto.req.portrayal.EmployeeListReq;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest.DeptProportion;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest.DeptTendency;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest.EmployeeProportion;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest.EmployeeTendency;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest.LevelProportion;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest.LevelTendency;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.latest.TeamOverviewResp;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
@Builder
public class CtoResp {

    /**
     * 核心数据指标
     */
    private List<TeamOverviewResp> teamOverviewResp;


    /**
     * 部门工程指标-部门占比统计
     */
    private DeptProportion deptProportion;

    /**
     * 部门工程指标-部门趋势统计
     */
    private DeptTendency deptTendency;

    /**
     * 部门工程指标-部门效率与稳定性统计
     */

    /**
     * 员工工程指标-员工占比统计
     */
    private EmployeeProportion employeeProportion;

    /**
     * 员工工程指标-员工趋势统计
     */
    private EmployeeTendency employeeTendency;

    /**
     * 职级工程指标统计-职级占比统计
     */
    private LevelProportion levelProportion;

    /**
     * 职级工程指标统计-职级趋势统计
     */
    private LevelTendency levelTendency;
}
