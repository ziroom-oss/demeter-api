package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.po.dto.req.monthRept.DemeterCoreDataReq;
import com.ziroom.tech.demeterapi.po.dto.resp.monthRept.DemeterCoreDataResp;

import java.util.List;
import java.util.Map;

public interface MonthReportService {

    //SLA
    DemeterCoreDataResp getSLA(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq);

    //业务系统支持
    DemeterCoreDataResp getBusSupport(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq);

    //开发效能
    DemeterCoreDataResp getDevEffiToll(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq);

    //运维支持
    DemeterCoreDataResp getOapSupport(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq);

}
