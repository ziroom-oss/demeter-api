package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.CTOReq;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.EmployeeListReq;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.DailyTaskReq;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.EngineeringMetricReq;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.PortrayalInfoReq;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.CtoDevResp;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.CtoOmegaResp;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.CtoResp;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.DailyTaskResp;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.EngineeringMetricResp;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.PortrayalInfoResp;
import com.ziroom.tech.demeterapi.po.dto.resp.task.EmployeeListResp;
import com.ziroom.tech.demeterapi.service.PortraitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 员工画像
 * @author daijiankun
 */
@RestController
@Slf4j
@RequestMapping("api/portrait")
public class PortraitController {

    @Resource
    private PortraitService portraitService;

    /**
     * 员工列表（管理者）
     * @param employeeListReq 请求体
     * @return Resp
     */

    @PostMapping("/list")
    public Resp<List<EmployeeListResp>> getEmployeeList(@RequestBody EmployeeListReq employeeListReq) {
        List<EmployeeListResp> employeeList = portraitService.getEmployeeList(employeeListReq);
        return Resp.success(employeeList);
    }

    /**
     * 员工详情
     */
    @GetMapping("/user/{uid}")
    public Resp<UserDetailResp> getUserDetail(@PathVariable String uid) {
        UserDetailResp userDetailResp = portraitService.getEmployeeDetail(uid);
        return Resp.success(userDetailResp);
    }

    /**
     * 画像信息
     * @return
     */
    @PostMapping("/info")
    public Resp<PortrayalInfoResp> getPortrayalInfo(@RequestBody PortrayalInfoReq portrayalInfoReq) {
        return Resp.success(portraitService.getPortrayalInfo(portrayalInfoReq));
    }

    /**
     * 日常任务
     * @return Resp<DailyTaskResp>
     */
    @PostMapping("/task")
    public Resp<DailyTaskResp> getDailyTaskInfo(@RequestBody DailyTaskReq dailyTaskReq) {
        return Resp.success(portraitService.getDailyTaskInfo(dailyTaskReq));
    }

    /**
     * 工程指标
     * @return
     */
    @PostMapping("/metric")
    public Resp<EngineeringMetricResp> getEngineeringMetrics(@RequestBody EngineeringMetricReq engineeringMetricReq) {
        return Resp.success(portraitService.getEngineeringMetrics(engineeringMetricReq));
    }


    /**
     * CTO视角
     */
    @PostMapping("/cto")
    public Resp<CtoResp> getCtoData(@RequestBody CTOReq ctoReq) {
        return Resp.success(portraitService.getCtoData(ctoReq));
    }

    /**
     * CTO视角-开发类
     * @param ctoReq
     * @return
     */
    @PostMapping("/cto/dev")
    public Resp<CtoDevResp> getCtoDevData(@RequestBody CTOReq ctoReq) {
        return Resp.success(portraitService.getCtoDevData(ctoReq));
    }

    /**
     * CTO视角-项目类
     * @param ctoReq
     * @return
     */
    @PostMapping("/cto/project")
    public Resp getCtoProjectData(@RequestBody CTOReq ctoReq) {
        return Resp.success(portraitService.getCtoProjectData(ctoReq));
    }

    /**
     * CTO视角-发布类
     * @param ctoReq
     * @return
     */
    @PostMapping("/cto/omega")
    public Resp<CtoOmegaResp> getCtoOmegaData(@RequestBody CTOReq ctoReq) throws Exception {
        return Resp.success(portraitService.getCtoOmegaData(ctoReq));
    }

    @PostMapping("/cto/worktop")
    public Resp getWorktopOverview(@RequestBody CTOReq ctoReq) throws Exception {
        return Resp.success(portraitService.getWorktopOverview(ctoReq));
    }

}