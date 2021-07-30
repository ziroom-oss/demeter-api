package com.ziroom.tech.demeterapi.po.dto.req.ranking;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("排行榜请求体")
public class RankingReq {

    @ApiModelProperty("月份")
    private String searchMouth;
    @ApiModelProperty("图谱名称")
    private String name;

}
