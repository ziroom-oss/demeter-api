package com.ziroom.tech.demeterapi.po.dto.resp.rankings;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@Builder
@ApiModel("排行榜列表")
public class RankingResp {

    @ApiModelProperty("我的排名")
    private Integer myRanking;
    @ApiModelProperty("排名信息")
    private List<RankingInfo> rankingList;
//    @ApiModelProperty("title")
//    private String title;
    public RankingResp(Integer myRanking, List<RankingInfo> rankingList){
        this.myRanking = myRanking;
        this.rankingList = rankingList;
    }
    public RankingResp() {
    }

    @Override
    public String toString() {
        return "RankingResp{" +
                "myRanking=" + myRanking +
                ", rankingList=" + rankingList +
                '}';
    }
}
