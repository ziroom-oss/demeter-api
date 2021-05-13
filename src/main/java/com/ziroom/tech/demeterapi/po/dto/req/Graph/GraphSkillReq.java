package com.ziroom.tech.demeterapi.po.dto.req.Graph;

import com.google.common.base.Preconditions;
import com.ziroom.tech.demeterapi.dao.entity.GraphSkill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Data
@ApiModel("新增和编辑技能图谱")
public class GraphSkillReq {
    @ApiModelProperty("主键id，也是图谱编号")
    private Long id;

    @ApiModelProperty("图谱名称")
    private String graphName;

    @ApiModelProperty("启用状态")
    private Integer enable;

    @ApiModelProperty("职务")
    private Integer position;

    public GraphSkill getEntity() {
        GraphSkill graphSkill = new GraphSkill();
        if (Objects.nonNull(id)) {
            graphSkill.setId(id);
        }
        validate();
        graphSkill.setGraphName(graphName);
        graphSkill.setEnable(enable);
        graphSkill.setPosition(position);
        return graphSkill;
    }

    public void validate() {
        Preconditions.checkArgument(StringUtils.isNotBlank(graphName), "图谱名称不能为空");
        Preconditions.checkArgument(Objects.nonNull(enable), "启用状态不能为空");
        Preconditions.checkArgument(Objects.nonNull(position), "职务不能为空");
    }
}
