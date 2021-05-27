package com.ziroom.tech.demeterapi.po.dto.req.task;

import com.ziroom.tech.demeterapi.common.PageListReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务列表查询请求体
 * @author daijiankun
 */
@Data
@ApiModel("任务列表-查询请求体")
public class TaskListQueryReq extends PageListReq {

    @ApiModelProperty("任务类型")
    private Integer taskType;

    @ApiModelProperty("任务状态")
    private Integer taskStatus;

    @ApiModelProperty("发布人或接收人系统号")
    private String systemCode;

    @ApiModelProperty("任务名称/编号")
    private String nameOrNo;

    @ApiModelProperty("技能树id-技能点属性")
    private Long skillTreeId;

    @ApiModelProperty("技能点等级-技能点属性")
    private Integer skillPointLevel;
}
