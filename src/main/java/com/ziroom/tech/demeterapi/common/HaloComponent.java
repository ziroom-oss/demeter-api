package com.ziroom.tech.demeterapi.common;

import com.ziroom.tech.demeterapi.po.dto.req.halo.AuthReq;
import com.ziroom.tech.demeterapi.po.dto.resp.halo.AuthResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * 权限管理
 * 此处需要替换为自己的权限中心
 * 根据情况分配权限 demeter-super-admin 为管理员权限 demeter-dept-admin为部门leader权限
 * @author xuzeyu
 **/
@Component
@Slf4j
public class HaloComponent {

    private List<String> managerList = Arrays.asList("60028724","60010370");

    /**
     * @param authReq
     * @return
     */
    public AuthResp auth(AuthReq authReq) {
        AuthResp authResp = new AuthResp();
        if(managerList.contains(authReq.getUserCode())){
            authResp.getRoles().addAll(Collections.singletonList("demeter-super-admin"));
        }
        return authResp;
    }

}
