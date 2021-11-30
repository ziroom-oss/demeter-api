package com.ziroom.tech.demeterapi.open.login.service.impl;

import com.ziroom.tech.demeterapi.open.ehr.client.service.EhrServiceClient;
import com.ziroom.tech.demeterapi.open.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 登录服务
 * @author xuzeyu
 */
@Service("testLoginService")
public class TestLoginServiceImpl implements LoginService {

    @Autowired
    private EhrServiceClient ehrServiceClient;

}
