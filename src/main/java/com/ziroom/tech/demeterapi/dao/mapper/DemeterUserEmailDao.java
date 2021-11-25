package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.DemeterUserEmail;
import com.ziroom.tech.demeterapi.dao.entity.DemeterUserEmailExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

@Mapper
public interface DemeterUserEmailDao {

    public int countByExample(DemeterUserEmailExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(DemeterUserEmail record);

    public int insertSelective(DemeterUserEmail record);

    public List<DemeterUserEmail> selectByExample(DemeterUserEmailExample example);

    public DemeterUserEmail selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") DemeterUserEmail record, @Param("example") DemeterUserEmailExample example);

    public int updateByExample(@Param("record") DemeterUserEmail record, @Param("example") DemeterUserEmailExample example);

    public int updateByPrimaryKeySelective(DemeterUserEmail record);

    public int updateByPrimaryKey(DemeterUserEmail record);
}