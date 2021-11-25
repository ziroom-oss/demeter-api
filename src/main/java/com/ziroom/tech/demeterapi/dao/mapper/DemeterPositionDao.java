package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.DemeterPosition;
import com.ziroom.tech.demeterapi.dao.entity.DemeterPositionExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

@Mapper
public interface DemeterPositionDao {

    public int countByExample(DemeterPositionExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(DemeterPosition record);

    public int insertSelective(DemeterPosition record);

    public List<DemeterPosition> selectByExample(DemeterPositionExample example);

    public List<DemeterPosition> selectAll();

    public DemeterPosition selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") DemeterPosition record, @Param("example") DemeterPositionExample example);

    public int updateByExample(@Param("record") DemeterPosition record, @Param("example") DemeterPositionExample example);

    public int updateByPrimaryKeySelective(DemeterPosition record);

    public int updateByPrimaryKey(DemeterPosition record);
}