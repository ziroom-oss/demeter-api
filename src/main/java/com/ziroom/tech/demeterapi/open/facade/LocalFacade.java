package com.ziroom.tech.demeterapi.open.facade;

import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;

/**
 * 演示版本-用户登录信息缓存 key=userCode, value=用户信息
 * @author: xuzeyu
 */
@Slf4j
public class LocalFacade {

    private static Map<String, UserDetailResp> loginInfos = new HashMap<>();

    public static void saveLoginInfo(String userCode, UserDetailResp userDetailResp) {
        loginInfos.put(userCode, userDetailResp);
    }

    public static UserDetailResp getLoginInfo(String userCode){
        return loginInfos.get(userCode);
    }

    public static void delete(String userCode) {
        if(loginInfos.containsKey(userCode)){
            loginInfos.remove(userCode);
        }
    }

}
