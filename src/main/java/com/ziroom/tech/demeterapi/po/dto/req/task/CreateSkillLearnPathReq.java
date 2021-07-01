package com.ziroom.tech.demeterapi.po.dto.req.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lipp3
 * @date 2021/6/30 11:30
 * @Description
 */
@Data
@ApiModel("创建技能学习路径请求体")
public class CreateSkillLearnPathReq {

    @ApiModelProperty("技能编号")
    private Integer skillId;

    @ApiModelProperty("技能学习路径列表")
    private List<String> learnPathes;
}
