package com.ziroom.tech.demeterapi.common;

import com.ziroom.tech.demeterapi.po.dto.req.halo.AuthReq;
import com.ziroom.tech.demeterapi.po.dto.resp.halo.AuthResp;
import com.ziroom.tech.model.PermissionInfo;
import com.ziroom.tech.model.UserUnfoldPermInfo;
import com.ziroom.tech.service.HaloPermissionLoadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

/**
 * halo
 *
 * @author huangqiaowei
 * @date 2021-02-02 11:00
 **/
@Component
@Slf4j
public class HaloComponent {

    @Resource
    private HaloPermissionLoadService haloPermissionLoadService;

    /**
     * @param authReq
     * @return
     */
    public AuthResp auth(AuthReq authReq) {
        AuthResp authResp = new AuthResp();
        UserUnfoldPermInfo userUnfoldPermInfo = haloPermissionLoadService.getUserUnfoldPermInfo(authReq.getUserCode()
                , authReq.getAppId());
        List<PermissionInfo> permissionInfos = userUnfoldPermInfo.getPermissionInfos();
        // 深度优先遍历
        if (CollectionUtils.isNotEmpty(permissionInfos)) {
            Stack<PermissionInfo> stack = new Stack<>();
            permissionInfos.forEach(permissionInfo -> {
                stack.push(permissionInfo);
                while (!stack.empty()) {
                    PermissionInfo pop = stack.pop();
                    // 0 菜单 1 功能
                    if (Objects.equals(pop.getType(), 0)) {
                        authResp.getMenulist().add(pop.getUrl());
                    } else {
                        authResp.getFunctions().put(pop.getUrl(), "1");
                    }
                    if (CollectionUtils.isNotEmpty(pop.getChildrenList())) {
                        pop.getChildrenList().forEach(stack::push);
                    }
                }
            });
        }
        if (CollectionUtils.isNotEmpty(userUnfoldPermInfo.getRoles())) {
            authResp.getRoles().addAll(userUnfoldPermInfo.getRoles());
        }
        return authResp;
    }

}
