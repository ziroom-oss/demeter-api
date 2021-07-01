package com.ziroom.tech.demeterapi.po.dto.req.ehr;

import com.google.common.base.Preconditions;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author huangqiaowei
 * @date 2020-07-01 20:01
 **/
@Data
public class UserDetailReq {

    private String userCode;

    public void validate() {
        Preconditions.checkArgument(StringUtils.isNotBlank(userCode), "用户code不能为空");
    }
}
