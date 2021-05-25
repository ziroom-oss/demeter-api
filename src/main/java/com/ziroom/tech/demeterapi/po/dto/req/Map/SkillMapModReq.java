package com.ziroom.tech.demeterapi.po.dto.req.Map;

import com.google.common.base.Preconditions;
import com.ziroom.tech.demeterapi.dao.entity.SkillMap;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

@Data
@ApiModel
public class SkillMapModReq {
    @ApiModelProperty("图谱编号")
    private Long id;
    @ApiModelProperty("图谱名称")
    private String name;
    @ApiModelProperty("启用状态")
    private Byte isEnable;
    @ApiModelProperty("职务 id")
    private Integer jobId;
    @ApiModelProperty("已删除")
    private Byte isDel;
    public SkillMap getEntity(SkillMapModReq skillMapModReq) {
        validate();
        SkillMap skillMap = new SkillMap();
        BeanUtils.copyProperties(skillMapModReq, skillMap);
        return skillMap;
    }
    public void validate() {
        Preconditions.checkArgument(Objects.nonNull(id), "图谱编号不能为空");
    }
}
