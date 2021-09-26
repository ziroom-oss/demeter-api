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
import java.util.ArrayList;
import java.util.List;

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
    @ApiOperation(value = "1、sla")
    @PostMapping("sLA")
    public Resp getSLA(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getSLA(demeterCoreDataReq,"SLA");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "2、核心接口TP95")
    @PostMapping("coreInterfaceTP95")
    public Resp<DemeterCoreDataResp> getCoreInterfaceTP95(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getSLA(demeterCoreDataReq,"核心接口TP95");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "3、数据中心宽带使用率")
    @PostMapping("dataCoreVlanUseRate")
    public Resp<DemeterCoreDataResp> getDataCoreVlanUseRate(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getSLA(demeterCoreDataReq,"数据中心宽带使用率");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "4、kbs集群资源使用率")
    @PostMapping("kbsClusterUserRate")
    public Resp<DemeterCoreDataResp> getKBSClusterUserRate(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getSLA(demeterCoreDataReq,"kbs集群资源使用率");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "5、p1故障次数")
    @PostMapping("p1IncidenceTime")
    public Resp<DemeterCoreDataResp> getP1IncidenceTime(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getSLA(demeterCoreDataReq,"P1故障次数");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "6、p2故障次数")
    @PostMapping("p2IncidenceTime")
    public Resp<DemeterCoreDataResp> getP2IncidenceTime(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getSLA(demeterCoreDataReq,"P2故障次数");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "7、慢sql数量")
    @PostMapping("slowSQLNum")
    public Resp<DemeterCoreDataResp> getSlowSQLNum(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getSLA(demeterCoreDataReq,"慢SQL数量");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "8、网关rt超1s的接口数")
    @PostMapping("gatewayMore1s")
    public Resp<DemeterCoreDataResp> getGatewayMore1s(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getSLA(demeterCoreDataReq,"网关RT超1秒的接口数");
        return Resp.success(coreDataResp);
    }

    /**
     *  业务系统支持
     */
    @ApiOperation(value = "9、短信，发送总数")
    @PostMapping("sendSum")
    public Resp<DemeterCoreDataResp> getSendSum(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getBusSupport(demeterCoreDataReq,"发送总数");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "10、直播，时长")
    @PostMapping("liveTime")
    public Resp<DemeterCoreDataResp> getLiveTime(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getBusSupport(demeterCoreDataReq,"时长");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "11、直播，观众数")
    @PostMapping("liveViewerNum")
    public Resp<DemeterCoreDataResp> getLiveViewerNum(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getBusSupport(demeterCoreDataReq,"观众数");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "12、AB号，AB号")
    @PostMapping("abNum")
    public Resp<DemeterCoreDataResp> getABNum(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getBusSupport(demeterCoreDataReq,"AB号");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "13、AB号，开发当量")
    @PostMapping("devEquiv")
    public Resp<DemeterCoreDataResp> getDevEquiv(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getBusSupport(demeterCoreDataReq,"开发当量");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "14、AB测试，实验数量")
    @PostMapping("abTestNum")
    public Resp<DemeterCoreDataResp> getABTestNum(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getBusSupport(demeterCoreDataReq,"实验数量");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "15、AB号，接口调用次数")
    @PostMapping("interfaceCallTime")
    public Resp<DemeterCoreDataResp> getInterfaceCallTime(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getBusSupport(demeterCoreDataReq,"接口调用次数");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "16、IM，IM采纳率")
    @PostMapping("iMAdaptedRate")
    public Resp<DemeterCoreDataResp> getIMAdaptedRate(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getBusSupport(demeterCoreDataReq,"IM采纳率");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "17、IM，房源推荐采纳率")
    @PostMapping("houseRecommendAdaptedRate")
    public Resp<DemeterCoreDataResp> getHouseRecommendAdaptedRate(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getBusSupport(demeterCoreDataReq,"房源推荐采纳率");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "18、IM，IM会话客户量")
    @PostMapping("iMSessionAdaptedRate")
    public Resp<DemeterCoreDataResp> getIMSessionAdaptedRate(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getSLA(demeterCoreDataReq,"IM会话客户量");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "19、IM，会话客户转签约率")
    @PostMapping("iMSessiontoqyRate")
    public Resp<DemeterCoreDataResp> getIMSessiontoqyRate(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getBusSupport(demeterCoreDataReq,"会话客户转签约率");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "20、PUSH，push总数")
    @PostMapping("allPushs")
    public Resp<DemeterCoreDataResp> getAllPushs(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getBusSupport(demeterCoreDataReq,"PUSH总数");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "21、PUSH，C端IOS送达率")
    @PostMapping("cIOSSenderRate")
    public Resp<DemeterCoreDataResp> getCIOSSenderRate(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getBusSupport(demeterCoreDataReq,"C端IOS送达率");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "22、PUSH，C端安卓送达率")
    @PostMapping("cAnddriorSenderRate")
    public Resp<DemeterCoreDataResp> getCAnddriorSenderRate(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getBusSupport(demeterCoreDataReq,"C端安卓送达率");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "23、PUSH，B端送达率")
    @PostMapping("bSenderRate")
    public Resp<DemeterCoreDataResp> getBSenderRate(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getBusSupport(demeterCoreDataReq,"B端送达率");
        return Resp.success(coreDataResp);
    }

    /**
     * 开发效能工具
     * @param demeterCoreDataReq
     * @return
     */
    @ApiOperation(value = "24、混沌工程，演练次数")
    @PostMapping("performTime")
    public Resp<DemeterCoreDataResp> getPerformTime(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getDevEffiToll(demeterCoreDataReq,"演练次数");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "25、混沌工程，故障覆盖类型")
    @PostMapping("incidenceMergeType")
    public Resp<DemeterCoreDataResp> getIncidenceMergeType(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getDevEffiToll(demeterCoreDataReq,"故障覆盖类型");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "26、omega，发布数")
    @PostMapping("publishNum")
    public Resp<DemeterCoreDataResp> getPublishNum(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getDevEffiToll(demeterCoreDataReq,"发布数");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "27、omega，回滚数")
    @PostMapping("rollbackNum")
    public Resp<DemeterCoreDataResp> getRollbackNum(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getDevEffiToll(demeterCoreDataReq,"回滚数");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "28、故障警告，异常事件数")
    @PostMapping("abnornalEventNum")
    public Resp<DemeterCoreDataResp> getAbnornalEventNum(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getDevEffiToll(demeterCoreDataReq,"异常事件数");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "29、故障警告，异常触发数")
    @PostMapping("abnornalTrigerNum")
    public Resp<DemeterCoreDataResp> getAbnornalTrigerNum(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getDevEffiToll(demeterCoreDataReq,"异常触发数");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "30、故障警告，A类应用接入量")
    @PostMapping("connectNum")
    public Resp<DemeterCoreDataResp> getAConnectNum(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getDevEffiToll(demeterCoreDataReq,"A类应用接入量");
        return Resp.success(coreDataResp);
    }

    /**
     * 运维支持数据
     * @param demeterCoreDataReq
     * @return
     */
    @ApiOperation(value = "31、工单，工单数")
    @PostMapping("orderNum")
    public Resp<DemeterCoreDataResp> getOrderNum(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getOapSupport(demeterCoreDataReq,"工单数");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "32、工单，工单自动转化率")
    @PostMapping("atoTransform")
    public Resp<DemeterCoreDataResp> getAtoTransform(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getOapSupport(demeterCoreDataReq,"工单自动化率");
        return Resp.success(coreDataResp);
    }
    @ApiOperation(value = "33、服务台，服务台满意度")
    @PostMapping("servicePlatformSatisfaction")
    public Resp<DemeterCoreDataResp> getServicePlatformSatisfaction(@RequestBody DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataResp coreDataResp = monthReportService.getOapSupport(demeterCoreDataReq,"服务台满意度");
        return Resp.success(coreDataResp);
    }


}
