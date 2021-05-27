package com.ziroom.tech.demeterapi.po.dto.req.Tree;

import com.google.common.base.Preconditions;
import com.ziroom.tech.demeterapi.dao.entity.SkillTree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

@Data
@ApiModel("创建技能树节点")
public class SkillTreeCreateReq {
    @ApiModelProperty("父节点 id")
    private Integer parentId;

    @ApiModelProperty("节点名称")
    private String name;

    @ApiModelProperty("节点排序")
    private Byte sort;

    public SkillTree getEntity(SkillTreeCreateReq skillTreeCreateReq) {
        skillTreeCreateReq.validate();
        SkillTree skillTree = new SkillTree();
        BeanUtils.copyProperties(skillTreeCreateReq, skillTree);
        return skillTree;
    }

    public void validate() {
        Preconditions.checkArgument(Objects.nonNull(parentId), "父节点 id 不能为空");
        Preconditions.checkArgument(Objects.nonNull(name), "节点名称不能为空");
        Preconditions.checkArgument(Objects.nonNull(sort), "排序值不能为空");
    }
}
