package com.ziroom.tech.demeterapi.open.auth.service.impl;

import com.ziroom.tech.demeterapi.open.auth.dao.AuthUserMapper;
import com.ziroom.tech.demeterapi.open.auth.entity.AuthUserEntity;
import com.ziroom.tech.demeterapi.open.login.model.OperatorContext;
import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.common.utils.ModelResultUtil;
import com.ziroom.tech.demeterapi.open.auth.model.AuthModelResp;
import com.ziroom.tech.demeterapi.open.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限管理
 * 此处根据需要替换为自己的权限中心
 * 根据情况分配权限 参见 CurrentRole 枚举类 demeter-super-admin 为管理员权限 demeter-dept-admin为部门leader权限 plain为普通用户
 * @author xuzeyu
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthUserMapper authUserMapper;

    @Override
    public ModelResult<AuthModelResp> getAuth() {
        AuthModelResp authModelResp = new AuthModelResp();
        String operator = OperatorContext.getOperator();
        List<AuthUserEntity> roleList = authUserMapper.getRoles(operator);
        if(CollectionUtils.isNotEmpty(roleList)){
            List<String> roles = roleList.stream().map(AuthUserEntity::getRole).collect(Collectors.toList());
            authModelResp.getRoles().addAll(roles);
        }
        return ModelResultUtil.success(authModelResp);
    }
}
