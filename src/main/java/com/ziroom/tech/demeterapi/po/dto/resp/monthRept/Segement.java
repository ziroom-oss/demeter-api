package com.ziroom.tech.demeterapi.po.dto.resp.monthRept;

import com.ziroom.tech.demeterapi.dao.entity.DemeterCoreData;
import io.swagger.annotations.Api;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Api("子级集合")
public class Segement {

    private String coreName;

    //core_sys_name 为空与不为空
    private List<DemeterCoreData> demeterCoreDataList;

}
