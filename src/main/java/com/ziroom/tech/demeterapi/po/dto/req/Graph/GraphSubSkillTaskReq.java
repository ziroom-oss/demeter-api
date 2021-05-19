package com.ziroom.tech.demeterapi.po.dto.req.Graph;

import com.google.common.base.Preconditions;
import com.ziroom.tech.demeterapi.dao.entity.GraphSubSkillTask;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

@Data
@ApiModel("新增和编辑技能图谱的子技能")
public class GraphSubSkillTaskReq {
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("父级技能 id")
    private Long skillId;

    @ApiModelProperty("任务 id")
    private Long taskId;

    @ApiModelProperty("子技能等级")
    private Integer level;

    @ApiModelProperty("职务等级 T 序列")
    private Integer jobLevel;

    public GraphSubSkillTask getEntity() {
        GraphSubSkillTask graphSubSkillTask = new GraphSubSkillTask();
        if (Objects.nonNull(id)) {
            graphSubSkillTask.setId(id);
        }
        validate();
        graphSubSkillTask.setSkillId(skillId);
        graphSubSkillTask.setTaskId(taskId);
        graphSubSkillTask.setLevel(level);;
        graphSubSkillTask.setJobLevel(jobLevel);
        return graphSubSkillTask;
    }

    public void validate() {
        Preconditions.checkArgument(Objects.nonNull(skillId), "父级技能 skillId 不能为空");
        Preconditions.checkArgument(Objects.nonNull(taskId), "任务 id 不能为空");
        Preconditions.checkArgument(Objects.nonNull(level), "子技能等级不能为空");
        Preconditions.checkArgument(Objects.nonNull(jobLevel), "职务等级不能为空");
    }
}
