package com.ziroom.tech.demeterapi.common;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;

/**
 * 列表返回类
 *
 * @author huangqiaowei
 * @date 2020-02-13 16:54
 **/
@Data
@ApiModel("分页结果")
public class PageListResp<T> {

    @ApiModelProperty("总数")
    private Integer total;

    @ApiModelProperty("数据")
    private List<T> data;

    public static <T> PageListResp<T> emptyList() {
        PageListResp<T> pageListResp = new PageListResp<>();
        List<T> result = Lists.newArrayList();
        pageListResp.setData(result);
        pageListResp.setTotal(NumberUtils.INTEGER_ZERO);
        return pageListResp;
    }
}
