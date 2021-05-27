package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.EmployeeListReq;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.DailyTaskReq;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.PortrayalInfoReq;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.DailyTaskResp;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.PortrayalInfoResp;
import com.ziroom.tech.demeterapi.po.dto.resp.task.EmployeeListResp;
import com.ziroom.tech.demeterapi.service.PortraitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/metrics")
    public Resp<Object> getEngineeringMetrics() {
        return Resp.success();
    }

}
