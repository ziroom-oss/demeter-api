package com.ziroom.tech.demeterapi.po.dto.req.task;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@ApiOperation("修改学习清单")
@Data
public class ModifySkillLearnManifestReq {
    @ApiModelProperty("学习清单 id")
    private Long id;

    @ApiModelProperty("学习清单名称")
    private String name;

    @ApiModelProperty("学习周期开始")
    private Date learnPeriodStart;

    @ApiModelProperty("学习周期结束")
    private Date learnPeriodEnd;

    @ApiModelProperty("学习者")
    private String learnerUid;

    @ApiModelProperty("学习技能点")
    private List<Long> skills;

    @ApiModelProperty("学习技能点路径")
    private Map<String, List<String>> skillPaths;
}
