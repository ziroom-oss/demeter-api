package com.ziroom.tech.demeterapi.po.dto.req.ehr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenx34
 * @date 2020/8/19 16:34
 */
@Data
@ApiModel("获取部门列表请求体")
public class EhrOrgListReq {

    @ApiModelProperty("部门层级, 0集团, 1虚拟层, 11 城市业务战区, 2 城市下战区／职能中心, 3 业务大区／职能部门, 4 组默认为所有层级")
    private Integer orgLevel;

    @ApiModelProperty("页数 默认为第一页")
    private Integer page = 1;

    @ApiModelProperty("每页记录数，默认为10条，最大不超过100")
    private Integer size = 10;
}
