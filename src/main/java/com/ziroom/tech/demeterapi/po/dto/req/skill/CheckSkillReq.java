package com.ziroom.tech.demeterapi.po.dto.req.skill;

import com.ziroom.tech.demeterapi.common.PageListReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class CheckSkillReq extends PageListReq {

    @ApiModelProperty("接收人系统号")
    private String systemCode;

    @ApiModelProperty("技能点名称、编号")
    private String nameOrNo;

    private Integer taskStatus;
}
