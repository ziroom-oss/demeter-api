package com.ziroom.tech.demeterapi.po.dto.req.ehr;

import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author chenx34
 */
@Data
public class EhrNextOrgReq {

    /**
     * 部门code
     */
    private String deptId;

    public void validate() {
        Preconditions.checkArgument(StringUtils.isNotBlank(deptId), "部门编码不能为空");
    }
}
