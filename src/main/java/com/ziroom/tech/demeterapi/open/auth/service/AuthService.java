package com.ziroom.tech.demeterapi.open.auth.service;

import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.auth.model.AuthModelResp;

/**
 * @author xuzeyu
 */
public interface AuthService {

    /**
     * 获取当前登录用户权限
     */
    ModelResult<AuthModelResp> getAuth();
}
