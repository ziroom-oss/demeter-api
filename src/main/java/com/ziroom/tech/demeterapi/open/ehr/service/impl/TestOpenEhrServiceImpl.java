package com.ziroom.tech.demeterapi.open.ehr.service.impl;

import com.alibaba.fastjson.JSON;
import com.ziroom.tech.demeterapi.common.exception.BusinessException;
import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.ehr.client.service.EhrServiceClient;
import com.ziroom.tech.demeterapi.open.ehr.dto.EhrUserInfoDto;
import com.ziroom.tech.demeterapi.open.ehr.service.OpenEhrService;
import com.ziroom.tech.demeterapi.open.common.utils.ModelResultUtil;
import com.ziroom.tech.demeterapi.open.login.converter.LoginConverter;
import com.ziroom.tech.demeterapi.open.login.dto.UserInfoDto;
import com.ziroom.tech.demeterapi.open.login.param.LogInUserParam;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务
 * @author xuzeyu
 */
@Slf4j
@Service("testOpenEhrService")
public class TestOpenEhrServiceImpl implements OpenEhrService {

    @Autowired
    private EhrServiceClient ehrServiceClient;

    /**
     * 个人基本信息展现
     */
    public ModelResult<EhrUserInfoDto> getPortraitUserInfo(String uid) {
        ModelResult<UserDetailResp> userInfoModelResult = ehrServiceClient.getUserInfo(uid);
        if(!userInfoModelResult.isSuccess()){
            EhrUserInfoDto ehrUserInfoDto = new EhrUserInfoDto();
            ehrUserInfoDto.setUsername("马可波罗");
            ehrUserInfoDto.setEmail("xxx@ziroom.com");
            ehrUserInfoDto.setEducation("硕士");
            ehrUserInfoDto.setHireDays(100);
            ehrUserInfoDto.setJob("java开发工程师");
            ehrUserInfoDto.setPosition("T3");
            ehrUserInfoDto.setSkills("线程池,zookeeper");
            return ModelResultUtil.success(ehrUserInfoDto);
        }
        UserDetailResp userDetailResp = userInfoModelResult.getResult();
        EhrUserInfoDto ehrUserInfoDto = new EhrUserInfoDto();
        ehrUserInfoDto.setUsername(userDetailResp.getUserName());
        ehrUserInfoDto.setEmail(userDetailResp.getEmail());
        ehrUserInfoDto.setEducation("本科");
        ehrUserInfoDto.setHireDays(100);
        ehrUserInfoDto.setJob("java开发工程师");
        ehrUserInfoDto.setPosition("T3");
        ehrUserInfoDto.setSkills("线程池, zookeeper");
        return ModelResultUtil.success(ehrUserInfoDto);
    }

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

    /**
     * 登录专属 根据登录名/密码查询用户
     */
    public ModelResult<UserDetailResp> getUserInfoByLogin(LogInUserParam loginParam) {
        ModelResult<UserDetailResp> userInfoModelResult = ehrServiceClient.getUserInfoByLogin(loginParam);
        if(!userInfoModelResult.isSuccess()){
            log.error("[OpenEhrService] ehrServiceClient.getUserInfoByLogin result is {}", JSON.toJSONString(userInfoModelResult));
            throw new BusinessException("查询用户信息失败");
        }
        return userInfoModelResult;
    }

}
