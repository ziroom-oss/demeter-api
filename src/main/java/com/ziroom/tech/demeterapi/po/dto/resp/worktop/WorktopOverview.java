package com.ziroom.tech.demeterapi.po.dto.resp.worktop;

import lombok.Builder;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
@Builder
public class WorktopOverview {

    private String workTimeCount;

    private String projectCount;

    private String taskCount;

    private String projectAvg;

    private String taskAvg;

    private DepartmentData departmentData;
}
