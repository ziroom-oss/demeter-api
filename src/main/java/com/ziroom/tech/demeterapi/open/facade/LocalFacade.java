package com.ziroom.tech.demeterapi.open.facade;

import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户登录信息缓存 key=token, value=用户信息
 * @author: xuzeyu
 */
@Slf4j
public class LocalFacade {

    private static Map<String, UserDetailResp> loginInfos = new HashMap<>();

    public static void saveLoginInfo(String token, UserDetailResp userDetailResp) {
        loginInfos.put(token, userDetailResp);
    }

    public static UserDetailResp getLoginInfo(String token){
        return loginInfos.get(token);
    }

    private static void delete(String token) {
        if(loginInfos.containsKey(token)){
            loginInfos.remove(token);
        }
    }

}
