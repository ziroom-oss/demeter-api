package com.ziroom.tech.demeterapi.open.ehr.service;

import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.ehr.dto.EhrUserInfoDto;
import com.ziroom.tech.demeterapi.open.login.dto.UserInfoDto;
import com.ziroom.tech.demeterapi.open.login.param.LogInUserParam;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;

/**
 * @author xuzeyu
 */
public interface OpenEhrService {

    ModelResult<EhrUserInfoDto> getPortraitUserInfo(String uid);

    ModelResult<UserInfoDto> getUserInfo(String operator);

    ModelResult<UserDetailResp> getUserInfoByLogin(LogInUserParam loginParam);

}
