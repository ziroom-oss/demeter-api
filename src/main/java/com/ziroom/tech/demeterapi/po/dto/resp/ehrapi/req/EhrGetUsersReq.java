package com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.req;

import com.google.common.base.Preconditions;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 请求EHR getUsers接口用的请求体
 *
 * @author chenx34
 * @date 2020/4/23 14:54
 */
@Data
public class EhrGetUsersReq {

    private String deptId;

    public void validate() {
        Preconditions.checkArgument(StringUtils.isNotBlank(deptId), "部门编码不能为空");
    }
}
