package com.ziroom.tech.demeterapi.po.dto.req.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author lipp3
 * @date 2021/6/30 11:02
 * @Description
 */
@Data
@ApiModel("创建学习清单请求体")
public class CreateSkillLearnManifestReq {

    @ApiModelProperty("学习清单名称")
    private String name;

    @ApiModelProperty("学习周期")
    private String learnPeriod;

    @ApiModelProperty("学习者")
    private String learner;

    @ApiModelProperty("学习技能点")
    private List<Long> skills;

    @ApiModelProperty("学习技能点路径")
    private Map<String, List<String>> skillPathes;
}
