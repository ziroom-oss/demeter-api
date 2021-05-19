package com.ziroom.tech.demeterapi.service.impl;

import com.ziroom.tech.demeterapi.common.HaloComponent;
import com.ziroom.tech.demeterapi.common.OperatorContext;
import com.ziroom.tech.demeterapi.po.dto.req.halo.AuthReq;
import com.ziroom.tech.demeterapi.po.dto.resp.halo.AuthResp;
import com.ziroom.tech.demeterapi.service.HaloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author daijiankun
 */
@Service
@Slf4j
public class HaloServiceImpl implements HaloService {

    @Resource
    private HaloComponent haloComponent;

    @Override
    public AuthResp getAuth() {
        AuthReq authReq = new AuthReq();
        authReq.setUserCode(OperatorContext.getOperator());
        authReq.setAppId("demeter-api");
        return haloComponent.auth(authReq);
    }
}
