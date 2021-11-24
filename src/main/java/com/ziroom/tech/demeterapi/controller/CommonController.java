package com.ziroom.tech.demeterapi.controller;

import com.alibaba.fastjson.JSON;
import com.ziroom.tech.demeterapi.common.HaloComponent;
import com.ziroom.tech.demeterapi.common.OperatorContext;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.halo.AuthReq;
import com.ziroom.tech.demeterapi.po.dto.resp.halo.AuthResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author daijiankun
 */
@RestController
@Slf4j
@RequestMapping(value = "/api/common/")
public class CommonController {

    @Resource
    private HaloComponent haloComponent;

    @GetMapping("auth/v1")
    public Resp<AuthResp> getCurrentAuth() {
        AuthReq authReq = new AuthReq();
        authReq.setAppId("demeter-api");
        authReq.setUserCode(OperatorContext.getOperator());
        log.info("GatewayApi.auth params:{}", JSON.toJSONString(authReq));
        return Resp.success(haloComponent.auth(authReq));
    }

}
