package com.ziroom.tech.demeterapi.po.dto.req.monthRept;

import cn.hutool.core.date.DateTime;
import lombok.Data;

import javax.annotation.Nullable;

@Data
public class DemeterCoreDataReq {

    //部门查询
    private String departmentCode;

    //时间查询
    @Nullable
    private String createTimeStart;

    private String createTimeEnd;



}
