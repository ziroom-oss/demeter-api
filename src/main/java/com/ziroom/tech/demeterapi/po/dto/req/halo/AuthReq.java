package com.ziroom.tech.demeterapi.po.dto.req.halo;

import com.google.common.base.Preconditions;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * auth
 *
 * @author huangqiaowei
 * @date 2021-02-02 15:59
 **/
@Data
public class AuthReq {

    private String appId;

    private String userCode;

    public void validate() {
        Preconditions.checkArgument(StringUtils.isNotBlank(appId), "appId不能为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(userCode), "userCode不能为空");
    }
}
