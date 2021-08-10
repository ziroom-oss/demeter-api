package com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.req;

import lombok.Data;

/**
 * 查询部门下人员
 *
 * @author huangqiaowei
 * @date 2020-06-03 14:47
 **/
@Data
public class EhrEmpListReq {

    private String empCodeNameAdcode;

    private String phone;

    private String orgCode;

    private String orgName;

    private String jobName;

    private String seriesCode;

    private Integer page;

    private Integer size;
}
