package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.po.dto.resp.halo.AuthResp;

/**
 * @author daijiankun
 */
public interface HaloService {

    /**
     * 获取当前登录用户权限
     * @return 权限
     */
    AuthResp getAuth();
}
