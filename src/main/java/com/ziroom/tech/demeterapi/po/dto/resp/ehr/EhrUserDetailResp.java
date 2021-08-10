package com.ziroom.tech.demeterapi.po.dto.resp.ehr;

import com.ziroom.tech.demeterapi.dao.entity.Jobs;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 请求ehr用户详情的返回体
 *
 * @author chenx34
 * @date 2020/5/6 17:01
 */
@Data
@ApiModel("请求ehr用户详情的返回体")
public class EhrUserDetailResp {

    @ApiModelProperty("员工姓名")
    private String name;

    @ApiModelProperty("系统号")
    private String code;

    @ApiModelProperty("邮箱地址")
    private String email;

    @ApiModelProperty("头像地址")
    private String avatar;

    @ApiModelProperty("组织架构代码")
    private String groupCode;

    @ApiModelProperty("组织机构名称")
    private String groupName;

    @ApiModelProperty("部门树的树路径")
    private String treePath;

    @ApiModelProperty("主职标识")
    private String jobIndicator;

    @ApiModelProperty("职级名称")
    private String levelName;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("部门code")
    private String deptCode;

    @ApiModelProperty("中心名称")
    private String center;

    @ApiModelProperty("中心id")
    private String centerId;

    @ApiModelProperty("职务描述")
    private String desc;

    @ApiModelProperty("公司编码")
    private String setId;

    @ApiModelProperty("职务编码")
    private String jobCode;

    @ApiModelProperty("职务编码-New")
    private String jobCodeNew;

}
