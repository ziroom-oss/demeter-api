package com.ziroom.tech.demeterapi.service;

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

import com.ziroom.tech.demeterapi.po.dto.resp.worktop.WorktopOverview;
import java.util.Date;
import java.util.List;

/**
 * @author daijiankun
 */
public interface PortraitService {

    /**
     * 员工列表（管理者）
     * @param employeeListReq 请求体
     * @return EmployeeListResp
     */
    List<EmployeeListResp> getEmployeeList(EmployeeListReq employeeListReq);

    /**
     * 员工详情（当前登陆用户）
     * @param uid
     * @return
     */
    UserDetailResp getEmployeeDetail(String uid);

    /**
     * 查询当前登录人的指派类任务指标
     * @param dailyTaskReq 请求体
     * @return EngineeringMetricsResp
     */
    DailyTaskResp getDailyTaskInfo(DailyTaskReq dailyTaskReq);

    /**
     * 查询画像信息
     * @param portrayalInfoReq 请求体
     */
    PortrayalInfoResp getPortrayalInfo(PortrayalInfoReq portrayalInfoReq);

    EngineeringMetricResp getEngineeringMetrics(EngineeringMetricReq engineeringMetricReq);

    CtoResp getCtoData(CTOReq ctoReq);

    CtoDevResp getCtoDevData(CTOReq ctoReq);

    Object getCtoProjectData(CTOReq ctoReq);

    CtoOmegaResp getCtoOmegaData(CTOReq ctoReq) throws Exception;

    WorktopOverview getWorktopOverview(CTOReq ctoReq) throws Exception;
}
