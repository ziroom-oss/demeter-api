package com.ziroom.tech.demeterapi.po.dto.req.task;

import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 指派类任务请求体
 * @author daijiankun
 */
@Data
@ApiModel("指派类任务请求体")
public class AssignTaskReq {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("是否立即生效")
    private Byte taskEffectiveImmediate;

    @ApiModelProperty("任务周期-开始时间")
    private Date taskStartTime;

    @ApiModelProperty("任务周期-结束时间")
    private Date taskEndTime;

    @ApiModelProperty("任务接收者")
    private List<String> taskReceiver;

    @ApiModelProperty("任务自身状态：关闭 开启")
    private Integer taskStatus;

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
    private MultipartFile attachment;

    @ApiModelProperty("备注")
    private String taskDescription;

    public void validateAdd() {
        Preconditions.checkArgument(StringUtils.isNotEmpty(taskName), "任务名称不能为空");
        Preconditions.checkArgument(Objects.nonNull(taskEndTime), "任务结束时间不能为空");
        Preconditions.checkArgument(Objects.nonNull(taskReward) && taskReward != 0, "任务成长值奖励不能为空");
        Preconditions.checkArgument(Objects.nonNull(needAcceptance), "任务是否需要验收不能为空");
    }
}
