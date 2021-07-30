package com.ziroom.tech.demeterapi.po.dto.resp.rankings;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("排行榜列表")
public class RankingResp {

    @ApiModelProperty("我的排名")
    private Long myRanking;
    @ApiModelProperty("排名信息")
    private List<RankingInfo> rankingList;

}
