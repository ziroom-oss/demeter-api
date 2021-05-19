package com.ziroom.tech.demeterapi.po.dto.resp.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author daijiankun
 */
@Data
@ApiModel("我发布的-查询请求体")
@Builder
public class ReleaseQueryResp {

    @ApiModelProperty("任务id")
    private Long id;

    @ApiModelProperty("任务编号")
    private String taskNo;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("任务类型")
    private Integer taskType;

    @ApiModelProperty("任务类型名")
    private String taskTypeName;

    @ApiModelProperty("任务状态")
    private Integer taskStatus;

    @ApiModelProperty("任务状态名")
    private String taskStatusName;

    @ApiModelProperty("任务创建时间")
    private Date taskCreateTime;

    @ApiModelProperty("任务成长值")
    private Integer growthValue;

    @ApiModelProperty("任务接收者姓名")
    private String taskReceiverName;

    private String publisher;

    private String publisherName;
}
