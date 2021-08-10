package com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.resp;

import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class EhrApiJobs {

    private String contrastJobCode;

    private String jobCode;

    private String jobName;

    private String jobLevel;

    private String deptCode;

    private String deptName;

    private String orgTree;

    private String orgDesc;

    private Object hpsBusType;

    private String orgType;

    private String postCode;

    private String jobIndicator;

    private String cityCode;

    private String empType;
}
