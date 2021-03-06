package com.ziroom.tech.demeterapi.po.dto.req.task;

import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

/**
 * 技能点请求体
 * @author daijiankun
 */
@Data
@ApiModel("技能点请求体")
public class SkillTaskReq {

    @ApiModelProperty("主键")
    private Long id;

    /**
     * 任务发布者
     */
    private String publisher;

    /**
     * 任务修改人
     */
    private String modifyId;

    @ApiModelProperty("技能点名称")
    private String taskName;

    @ApiModelProperty("技能点状态")
    private Integer taskStatus;

    @ApiModelProperty("技能值奖励")
    private Integer skillReward;

    private List<Long> checkRoles;

    private List<String> taskFinishCondition;

    private Integer skillLevel;

    /**
     * 技能点附件
     */
    private MultipartFile attachment;

    private Integer skillTreeId;

    @ApiModelProperty("备注")
    private String taskRemark;

    public void validateAdd() {
        Preconditions.checkArgument(StringUtils.isNotEmpty(taskName), "技能点名称不能为空");
        Preconditions.checkArgument(Objects.nonNull(taskStatus), "技能点启用状态不能为空");
        Preconditions.checkArgument(Objects.nonNull(skillLevel) && skillLevel != 0, "技能点等级不能为空");
        Preconditions.checkArgument(Objects.nonNull(skillReward) && skillReward != 0, "技能点技能值奖励不能为空");
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(taskFinishCondition), "至少有一个技能点验收标准");
        Preconditions.checkArgument(Objects.nonNull(skillTreeId), "Please select one skill tree node at least.");
    }

    public void validateEdit() {
        Preconditions.checkArgument(Objects.nonNull(id), "主键不能为空");
        this.validateAdd();
    }
}
