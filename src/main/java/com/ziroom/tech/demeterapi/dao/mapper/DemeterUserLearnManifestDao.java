package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.DemeterUserLearnManifest;
import com.ziroom.tech.demeterapi.dao.entity.DemeterUserLearnManifestExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

@Mapper
public interface DemeterUserLearnManifestDao {

    public int countByExample(DemeterUserLearnManifestExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(DemeterUserLearnManifest record);

    public int insertSelective(DemeterUserLearnManifest record);

    public List<DemeterUserLearnManifest> selectByExample(DemeterUserLearnManifestExample example);

    public DemeterUserLearnManifest selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") DemeterUserLearnManifest record, @Param("example") DemeterUserLearnManifestExample example);

    public int updateByExample(@Param("record") DemeterUserLearnManifest record, @Param("example") DemeterUserLearnManifestExample example);

    public int updateByPrimaryKeySelective(DemeterUserLearnManifest record);

    public int updateByPrimaryKey(DemeterUserLearnManifest record);
}