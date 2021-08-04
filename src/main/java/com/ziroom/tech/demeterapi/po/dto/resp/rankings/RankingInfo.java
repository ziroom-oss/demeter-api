package com.ziroom.tech.demeterapi.po.dto.resp.rankings;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("排名列表")
public class RankingInfo {
//
//    @ApiModelProperty("排名")
//    private long sort;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("数量")
    private long num;
}
