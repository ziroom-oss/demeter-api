package com.ziroom.tech.demeterapi.po.dto.req.task;

import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 技能类任务请求体
 * @author daijiankun
 */
@Data
@ApiModel("技能类任务请求体")
public class SkillTaskReq {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("任务名")
    private String taskName;

    @ApiModelProperty("任务状态")
    private Integer taskStatus;

    @ApiModelProperty("技能值奖励")
    private Integer skillReward;

    private List<String> taskFinishCondition;

    @ApiModelProperty("附件")
    private String attachmentUrl;

    @ApiModelProperty("备注")
    private String taskRemark;

    public void validateAdd() {
        Preconditions.checkArgument(StringUtils.isNotEmpty(taskName), "任务名称不能为空");
        Preconditions.checkArgument(Objects.nonNull(taskStatus), "任务启用状态不能为空");
        Preconditions.checkArgument(Objects.nonNull(skillReward), "任务技能值奖励不能为空");
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(taskFinishCondition), "至少有一个任务完成条件");
    }
}
