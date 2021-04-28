package com.ziroom.tech.demeterapi.po.dto.req.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 技能类任务请求体
 * @author daijiankun
 */
@Data
@ApiModel("技能类任务请求体")
public class SkillTaskReq {

    @ApiModelProperty("任务名")
    private String taskName;

    @ApiModelProperty("任务状态")
    private Integer taskStatus;

    @ApiModelProperty("技能值奖励")
    private Integer skillReward;

    @ApiModelProperty("附件")
    private String attachmentUrl;

    @ApiModelProperty("备注")
    private String taskRemark;
}
