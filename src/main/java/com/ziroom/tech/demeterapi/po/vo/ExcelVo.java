package com.ziroom.tech.demeterapi.po.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author libingsi
 * @date 2021/7/19 20:56
 */
@ApiModel(description = "excel对象")
public class ExcelVo {

    @ApiModelProperty(value = "工作表名")
    private String sheetName;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "表头")
    private String[] headers;

    @ApiModelProperty(value = "数据")
    private List<String[]> data;

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public List<String[]> getData() {
        return data;
    }

    public void setData(List<String[]> data) {
        this.data = data;
    }
}
