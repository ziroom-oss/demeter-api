package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.po.dto.req.portrayal.EmployeeListReq;
import com.ziroom.tech.demeterapi.po.dto.resp.task.EmployeeListResp;

import java.util.List;

/**
 * @author daijiankun
 */
public interface PortrayalService {

    /**
     * 员工列表（管理者）
     * @param employeeListReq 请求体
     * @return EmployeeListResp
     */
    List<EmployeeListResp> getEmployeeList(EmployeeListReq employeeListReq);
}
