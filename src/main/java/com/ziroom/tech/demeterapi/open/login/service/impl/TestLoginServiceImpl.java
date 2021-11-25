package com.ziroom.tech.demeterapi.open.login.service.impl;

import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.common.utils.ModelResultUtil;
import com.ziroom.tech.demeterapi.open.ehr.client.service.EhrServiceClient;
import com.ziroom.tech.demeterapi.open.login.converter.LoginConverter;
import com.ziroom.tech.demeterapi.open.login.dto.UserInfoDto;
import com.ziroom.tech.demeterapi.open.login.service.LoginService;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
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


    @Override
    public ModelResult<UserInfoDto> getUserInfo(String operator) {
        ModelResult<UserDetailResp> userInfoModelResult = ehrServiceClient.getUserInfo(operator);
        if(!userInfoModelResult.isSuccess()){
            return ModelResultUtil.error("-1","未查询到用户信息");
        }
        UserDetailResp userDetailResp = userInfoModelResult.getResult();
        UserInfoDto userInfoDto = LoginConverter.EhrUserInfoConverter().apply(userDetailResp);
        return ModelResultUtil.success(userInfoDto);
    }
}
