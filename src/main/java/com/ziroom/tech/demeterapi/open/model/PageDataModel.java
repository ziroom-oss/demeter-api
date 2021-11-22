package com.ziroom.tech.demeterapi.open.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDataModel<T> {
    /**
     * 当前页
     */
    private Integer pageNum=1;

    /**
     * 每页记录数
     */
    private Integer pageSize=20;

    /**
     * 总数
     */
    private Integer total=1;

    /**
     * 总页数
     */
    private Integer pages=1;

    /**
     * 数据
     */
    private List<T> data = new ArrayList<T>();

}



