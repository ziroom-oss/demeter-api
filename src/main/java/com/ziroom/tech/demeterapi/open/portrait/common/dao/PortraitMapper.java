package com.ziroom.tech.demeterapi.open.portrait.common.dao;

import com.ziroom.tech.demeterapi.open.portrait.common.entity.PersonDevlopReportEntity;
import com.ziroom.tech.demeterapi.open.portrait.person.param.PortraitPersonReqParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuzeyu
 */
@Mapper
public interface PortraitMapper {

    List<PersonDevlopReportEntity> getPortraitPerson(@Param(value = "personReqParam") PortraitPersonReqParam personReqParam);

}
