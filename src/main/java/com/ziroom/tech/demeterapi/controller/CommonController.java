package com.ziroom.tech.demeterapi.controller;

import com.alibaba.fastjson.JSON;
import com.ziroom.tech.demeterapi.common.HaloComponent;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.halo.AuthReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author daijiankun
 */
@RestController
@Slf4j
public class CommonController {

    @Resource
    private HaloComponent haloComponent;

    //////////////////////////  halo ////////////////////////

    @PostMapping("v1/auth")
    public Resp auth(@RequestBody AuthReq authReq) {
        authReq.validate();
        log.info("GatewayApi.auth params:{}", JSON.toJSONString(authReq));
        return Resp.success(haloComponent.auth(authReq));
    }

}
