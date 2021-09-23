package com.ziroom.tech.demeterapi.po.dto.req.ranking;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("排行榜请求体")
public class RankingReq  {

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("开始时间")
    private Date startTime;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("当前用户uid")
    private String uid;
    @ApiModelProperty("多个用户uid")
    private List<String> uids;

    @ApiModelProperty("图谱名称")
    private Integer searchSkillMap;

}
