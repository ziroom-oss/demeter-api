package com.ziroom.tech.demeterapi.po.dto.req.ehr;

import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author chenx34
 * @date 2020/6/24 14:13
 */
@Data
@ApiModel("请求EHR getChildOrgs接口时的请求体")
public class EhrNextOrgReq {

    @ApiModelProperty(value = "部门编码", required = true)
    private String deptId;

    @ApiModelProperty(value = "公司编码")
    private String setId;

    public void validate() {
        Preconditions.checkArgument(StringUtils.isNotBlank(deptId), "部门编码不能为空");
    }
}
