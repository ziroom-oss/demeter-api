package com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.req;

import com.google.common.base.Preconditions;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 请求EHR getChildOrgs接口时的请求体
 *
 * @author chenx34
 * @date 2020/4/23 15:06
 */
@Data
public class EhrNextOrgReq {

    private String deptId;

    private String setId;

    public void validate() {
        Preconditions.checkArgument(StringUtils.isNotBlank(deptId), "部门编码不能为空");
    }
}
