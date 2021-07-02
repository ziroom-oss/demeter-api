package com.ziroom.tech.demeterapi.po.dto.req.task;

import com.ziroom.tech.demeterapi.common.PageListReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lipp3
 * @date 2021/6/30 10:52
 * @Description
 */
@Data
@ApiModel("分配技能任务-查询请求体")
public class GetSkillLearnManifestReq extends PageListReq {

    @ApiModelProperty("清单状态")
    private Integer status;

    @ApiModelProperty("清单编号")
    private Long manifestId;

    @ApiModelProperty("清单名称")
    private String manifestName;
}
