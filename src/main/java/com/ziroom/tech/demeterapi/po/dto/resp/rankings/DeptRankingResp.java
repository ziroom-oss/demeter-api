package com.ziroom.tech.demeterapi.po.dto.resp.rankings;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

@Data
@ApiModel("排行榜数据")
public class DeptRankingResp implements Serializable {

    @ApiModelProperty("部门名称")
    @JsonProperty("departmentName")
    private String deptName;

    @ApiModelProperty("数量")
    @JsonProperty("count")
    private Long value;

    @ApiModelProperty("部门编码")
    private String deptCode;

}
