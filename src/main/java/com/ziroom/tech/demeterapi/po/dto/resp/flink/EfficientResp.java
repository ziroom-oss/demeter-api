package com.ziroom.tech.demeterapi.po.dto.resp.flink;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
@ApiModel(description= "部门效率类数据")
public class EfficientResp {

    @ApiModelProperty(value = "项目平均开发周期")
    private String projectAverageTime;


    @ApiModelProperty(value = "功能平均开发周期")
    private String functionAverageTime;

    @ApiModelProperty(value = "bug平均修复时间")
    private String bugAverageFixTime;


    @ApiModelProperty(value = "处理需求数")
    private Integer processDemandCount;

    @ApiModelProperty(value = "解决bug数")
    private Integer fixBugCount;

    @ApiModelProperty(value = "完成项目数")
    private Integer completeProjectCount;
}
