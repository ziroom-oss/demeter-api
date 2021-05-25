package com.ziroom.tech.demeterapi.po.dto.req.Map;

import com.google.common.base.Preconditions;
import com.ziroom.tech.demeterapi.dao.entity.SkillMap;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Data
@ApiModel
public class SkillMapCreateReq {
    @ApiModelProperty("主键id，也是图谱编号")
    private Long id;

    @ApiModelProperty("图谱名称")
    private String name;

    @ApiModelProperty("启用状态")
    private Byte isEnable;

    @ApiModelProperty("职务")
    private Integer jobId;

    public SkillMap getEntity() {
        SkillMap skillMap = new SkillMap();
        if (Objects.nonNull(id)) {
            skillMap.setId(id);
        }
        validate();
        skillMap.setName(name);
        skillMap.setIsEnable(isEnable);
        skillMap.setJobId(jobId);
        return skillMap;
    }

    public void validate() {
        Preconditions.checkArgument(StringUtils.isNotBlank(name), "图谱名称不能为空");
        Preconditions.checkArgument(Objects.nonNull(isEnable), "启用状态不能为空");
        Preconditions.checkArgument(Objects.nonNull(jobId), "职务不能为空");
    }
}
