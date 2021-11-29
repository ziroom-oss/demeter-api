package com.ziroom.tech.demeterapi.open.auth.service.impl;

import com.ziroom.tech.demeterapi.open.auth.client.AuthServiceClient;
import com.ziroom.tech.demeterapi.common.OperatorContext;
import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.common.utils.ModelResultUtil;
import com.ziroom.tech.demeterapi.open.auth.param.AuthReqParam;
import com.ziroom.tech.demeterapi.open.auth.model.AuthModelResp;
import com.ziroom.tech.demeterapi.open.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xuzeyu
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthServiceClient authServiceClient;

    @Override
    public ModelResult<AuthModelResp> getAuth() {
        AuthReqParam authReqParam = new AuthReqParam();
        authReqParam.setUserCode(OperatorContext.getOperator());
        authReqParam.setAppId("demeter-api");
        AuthModelResp authModelResp = authServiceClient.auth(authReqParam);
        return ModelResultUtil.success(authModelResp);
    }
}
