package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.po.dto.req.portrayal.DailyTaskReq;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.DailyTaskResp;

/**
 * @author daijiankun
 */
public interface PortraitService {



    /**
     * 查询当前登录人的指派类任务指标
     * @param dailyTaskReq 请求体
     * @return EngineeringMetricsResp
     */
    DailyTaskResp getDailyTaskInfo(DailyTaskReq dailyTaskReq);


}
