package com.ziroom.tech.demeterapi.po.dto.req.Tree;

import com.google.common.base.Preconditions;
import com.ziroom.tech.demeterapi.dao.entity.SkillTree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

@Data
@ApiModel("更新技能树节点")
public class SkillTreeModReq {
    @ApiModelProperty("节点 id")
    private Integer id;

    @ApiModelProperty("节点名称")
    private String name;

    @ApiModelProperty("排序值")
    private Byte sort;

    @ApiModelProperty("父节点 id")
    private Byte parentId;

    @ApiModelProperty("已删除 0 否 1 是")
    private Byte isDel;

    public SkillTree getEntity(SkillTreeModReq skillTreeModReq) {
        skillTreeModReq.validate();
        SkillTree skillTree = new SkillTree();
        BeanUtils.copyProperties(skillTreeModReq, skillTree);
        return skillTree;
    }

    public void validate() {
        Preconditions.checkArgument(Objects.nonNull(id), "节点 id 不能为空");
    }
}
