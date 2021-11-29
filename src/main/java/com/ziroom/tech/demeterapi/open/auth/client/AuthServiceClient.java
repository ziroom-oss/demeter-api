package com.ziroom.tech.demeterapi.open.auth.client;

import com.ziroom.tech.demeterapi.open.auth.param.AuthReqParam;
import com.ziroom.tech.demeterapi.open.auth.model.AuthModelResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * 权限管理
 * 此处需要替换为自己的权限中心
 * 根据情况分配权限 参见 CurrentRole 枚举类 demeter-super-admin 为管理员权限 demeter-dept-admin为部门leader权限 plain为普通用户
 * @author xuzeyu
 **/
@Component
@Slf4j
public class AuthServiceClient {

    private List<String> managerList = Arrays.asList("60028724","60010370");

    /**
     * 获取用户的权限
     */
    public AuthModelResp auth(AuthReqParam authReqParam) {
        AuthModelResp authModelResp = new AuthModelResp();
        if(managerList.contains(authReqParam.getUserCode())){
            authModelResp.getRoles().addAll(Collections.singletonList("demeter-super-admin"));
        }
        return authModelResp;
    }

}
