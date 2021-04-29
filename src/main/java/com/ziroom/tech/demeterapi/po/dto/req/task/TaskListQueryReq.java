package com.ziroom.tech.demeterapi.po.dto.req.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务列表查询请求体
 * @author daijiankun
 */
@Data
@ApiModel("任务列表-查询请求体")
public class TaskListQueryReq {

    @ApiModelProperty("任务类型")
    private Integer taskType;

    @ApiModelProperty("任务状态")
    private Integer taskStatus;

    @ApiModelProperty("接收者系统号，由前端转换")
    private String systemCode;

    @ApiModelProperty("任务名称/编号")
    private String nameOrNo;

}
