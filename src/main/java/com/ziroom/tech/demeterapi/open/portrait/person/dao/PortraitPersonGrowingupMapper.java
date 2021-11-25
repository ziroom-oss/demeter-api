package com.ziroom.tech.demeterapi.open.portrait.person.dao;

import com.ziroom.tech.demeterapi.open.portrait.person.entity.PortraitPersonGrowingupEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuzeyu
 */
@Mapper
public interface PortraitPersonGrowingupMapper {

    List<PortraitPersonGrowingupEntity> getUserGrowingupInfo(@Param(value = "contributor") String contributor, @Param(value = "date") String date);

}
