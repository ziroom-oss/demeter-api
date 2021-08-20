package com.ziroom.tech.demeterapi.po.dto.resp.rankings;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@Builder
@ApiModel("排名列表")
public class RankingInfo{
//
//    @ApiModelProperty("排名")
//    private long sort;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("数量")
    private String num;

    public RankingInfo(String name, String num) {
        this.name = name;
        this.num = num;
    }
}
