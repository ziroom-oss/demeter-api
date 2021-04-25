package com.ziroom.tech.demeterapi.common;

import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 分页请求实体
 *
 * @author huangqiaowei
 * @date 2020-04-21 12:10
 **/
@Data
@ApiModel("分页实体")
public class PageListReq {

    @ApiModelProperty("页面大小")
    private Integer pageSize;

    @ApiModelProperty("页码")
    private Integer pageNumber;

    private Integer start;

    public void validate() {
        Preconditions.checkArgument(pageSize >= NumberUtils.INTEGER_ONE && pageSize <= 1000, "页面大小不合法");
        Preconditions.checkArgument(pageNumber >= NumberUtils.INTEGER_ONE, "页码大小不合法");
        this.start = (this.pageNumber - 1) * this.pageSize;
    }
}
