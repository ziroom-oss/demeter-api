package com.ziroom.tech.demeterapi.po.dto.req.Graph;

import com.google.common.base.Preconditions;
import com.ziroom.tech.demeterapi.dao.entity.GraphAreaSkill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Data
@ApiModel("新增和编辑技能领域")
public class GraphAreaSkillReq {
    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("技能图谱id")
    private Integer graphId;

    @ApiModelProperty("技能领域名称")
    private String skillAreaName;

    @ApiModelProperty("技能名称")
    private String skillName;

    public GraphAreaSkill getEntity() {
        GraphAreaSkill graphAreaSkill = new GraphAreaSkill();
        if (Objects.nonNull(id)) {
            graphAreaSkill.setId(id);
        }
        validate();
        graphAreaSkill.setGraphId(graphId);
        graphAreaSkill.setSkillAreaName(skillAreaName);
        graphAreaSkill.setSkillName(skillName);
        return graphAreaSkill;
    }

    public void validate() {
        Preconditions.checkArgument(Objects.nonNull(graphId), "技能图谱id不能为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(skillAreaName), "技能领域名称不能为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(skillName), "技能名称不能为空");
    }
}
