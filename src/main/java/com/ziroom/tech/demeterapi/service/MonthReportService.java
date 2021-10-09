package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.po.dto.req.monthRept.DemeterCoreDataReq;
import com.ziroom.tech.demeterapi.po.dto.resp.monthRept.DemeterCoreDataResp;

import java.util.List;
import java.util.Map;

public interface MonthReportService {

    //SLA
    Map<String, DemeterCoreDataResp> getSLA(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq);

    //业务系统支持
    Map<String, DemeterCoreDataResp> getBusSupport(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq);

    //开发效能
    Map<String, DemeterCoreDataResp> getDevEffiToll(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq);

    //运维支持
    Map<String, DemeterCoreDataResp> getOapSupport(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq);

}
