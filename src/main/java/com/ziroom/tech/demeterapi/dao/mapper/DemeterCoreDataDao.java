package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.DemeterCoreData;
import com.ziroom.tech.demeterapi.dao.entity.DemeterCoreDataExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

@Mapper
public interface DemeterCoreDataDao {

    public int countByExample(DemeterCoreDataExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(DemeterCoreData record);

    public int insertSelective(DemeterCoreData record);

    public List<DemeterCoreData> selectByExample(DemeterCoreDataExample example);

    public DemeterCoreData selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") DemeterCoreData record, @Param("example") DemeterCoreDataExample example);

    public int updateByExample(@Param("record") DemeterCoreData record, @Param("example") DemeterCoreDataExample example);

    public int updateByPrimaryKeySelective(DemeterCoreData record);

    public int updateByPrimaryKey(DemeterCoreData record);
}