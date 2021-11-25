package com.ziroom.tech.demeterapi.open.login.service;

import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.login.dto.UserInfoDto;

/**
 * @author xuzeyu
 */
public interface LoginService {

    ModelResult<UserInfoDto> getUserInfo(String operator);
}
