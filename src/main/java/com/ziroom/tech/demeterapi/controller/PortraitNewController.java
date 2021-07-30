package com.ziroom.tech.demeterapi.controller;

import com.ziroom.gelflog.spring.logger.LogHttpService;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.CTOReq;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.CtoResp;
import com.ziroom.tech.demeterapi.service.FlinkAnalysisService;
import com.ziroom.tech.demeterapi.service.PortraitService;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 员工画像
 * @author daijiankun
 */
@RestController
@Slf4j
@RequestMapping("api/portrait/new")
@LogHttpService
public class PortraitNewController {

    @Resource
    private PortraitService portraitService;
    @Resource
    private FlinkAnalysisService flinkAnalysisService;

    @PostMapping("/cto/overview")
    public Resp<CtoResp> getTeamData(@RequestBody CTOReq ctoReq) {

        CtoResp ctoResp = flinkAnalysisService.getCtoResp(ctoReq);
        return Resp.success(ctoResp);
    }

}
