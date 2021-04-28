package com.ziroom.tech.demeterapi.po.dto.req.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 指派类任务请求体
 * @author daijiankun
 */
@Data
@ApiModel("指派类任务请求体")
public class AssignTaskReq {

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("是否立即生效")
    private Byte taskEffectiveImmediate;

    @ApiModelProperty("任务周期-开始时间")
    private Date taskStartTime;

    @ApiModelProperty("任务周期-结束时间")
    private Date taskEndTime;

    @ApiModelProperty("任务接收者")
    private String taskReceiver;

    @ApiModelProperty("任务完成条件")
    private List<String> taskFinishCondition;

    @ApiModelProperty("成长值奖励")
    private Integer taskReward;

    @ApiModelProperty("是否邮件提醒")
    private Byte needEmailRemind;

    @ApiModelProperty("是否有惩罚")
    private Byte needPunishment;

    @ApiModelProperty("是否需要验收")
    private Byte needAcceptance;

    @ApiModelProperty("附件")
    private String taskAttachmentUrl;

    @ApiModelProperty("备注")
    private String taskDescription;
}
