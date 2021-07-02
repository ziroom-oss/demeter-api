package com.ziroom.tech.demeterapi.po.dto.resp.portrait;

import java.util.List;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class CtoProjectResp {

    /**
     * 数据概览-开发类
     */
    private DevOverviewStruct devOverviewStruct;

    /**
     * 部门工程指标统计
     */
    private List<DevStruct> departmentDevList;

    /**
     * 项目工程指标统计
     */
    private List<DevStruct> projectDevList;

    /**
     * 周期工程指标统计
     */
    private List<DevStruct> periodList;

    /**
     * 职级工程指标统计
     */
    private List<DevStruct> levelList;
}
