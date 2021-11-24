package com.ziroom.tech.demeterapi.po.dto.resp.rankings;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

@Data
@ApiModel("排名列表")
public class InfoRanking implements Serializable {

    @ApiModelProperty("名称")
    private String uid;
    private String departmentName;
    @ApiModelProperty("数量")
    private String count;

}
