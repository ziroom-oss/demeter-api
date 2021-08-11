package com.ziroom.tech.demeterapi.po.dto.resp.rankings;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@ApiModel("排名列表")
public class InfoRanking implements Serializable {

    @ApiModelProperty("姓名")
    private String uid;
    @ApiModelProperty("数量")
    private String count;

}
