package com.ziroom.tech.demeterapi.controller;

import com.ziroom.gelflog.spring.logger.LogHttpService;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.monthRept.DemeterCoreDataReq;
import com.ziroom.tech.demeterapi.po.dto.resp.monthRept.DemeterCoreDataResp;
import com.ziroom.tech.demeterapi.service.MonthReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Map;


@RestController
@Slf4j
@RequestMapping("/api/monthReport")
@Api(tags = "月报，key值必须与表core_name一一对应")
@LogHttpService
public class MonthReportController {

    @Resource
    private MonthReportService monthReportService;

    /**
     *  SLA
     */
    @ApiOperation(value = "0、all")
    @PostMapping("slaAll")
    public Resp<Map<String, DemeterCoreDataResp>> slaAll(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        return Resp.success(monthReportService.getSLA(demeterCoreDataReq, null));
    }

    @ApiOperation(value = "1、业务系统支持")
    @PostMapping("busSupportAll")
    public Resp<Map<String, DemeterCoreDataResp>> busSupportAll(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        return Resp.success(monthReportService.getBusSupport(demeterCoreDataReq, null));
    }

    @ApiOperation(value = "2、开发效能工具")
    @PostMapping("devEffiTollAll")
    public Resp<Map<String, DemeterCoreDataResp>> devEffiTollAll(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        return Resp.success(monthReportService.getDevEffiToll(demeterCoreDataReq, null));
    }

    @ApiOperation(value = "3、运维支持工具")
    @PostMapping("oapSupportAll")
    public Resp<Map<String, DemeterCoreDataResp>> oapSupportAll(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        return Resp.success(monthReportService.getOapSupport(demeterCoreDataReq, null));
    }


}
