package com.ziroom.tech.demeterapi.po.dto.resp.rankings;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@ApiModel("排行榜列表")
public class RankResp {


    @ApiModelProperty("我的排名")
    private Integer myRanking;
    @ApiModelProperty("排名信息")
    private List<RankingInfo> rankingList;
    //    @ApiModelProperty("title")
//    private String title;
    public RankResp(Integer myRanking, List<RankingInfo> rankingList){
        this.myRanking = myRanking;
        this.rankingList = rankingList;
    }
    public RankResp() {
    }

    @Override
    public String toString() {
        return "RankingResp{" +
                "myRanking=" + myRanking +
                ", rankingList=" + rankingList +
                '}';
    }
}
