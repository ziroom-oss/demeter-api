package com.ziroom.tech.demeterapi.po.dto.req.portrayal;

import com.ziroom.tech.demeterapi.common.PageListReq;
import lombok.Data;

import java.util.Date;

/**
 * @author daijiankun
 */
@Data
public class EmployeeListReq extends PageListReq {

    private Date startTime;

    private Date endTime;

    private String deptNo;

    private String jobNo;
}
