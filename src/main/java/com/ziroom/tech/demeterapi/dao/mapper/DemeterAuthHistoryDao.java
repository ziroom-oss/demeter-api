package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.DemeterAuthHistory;
import com.ziroom.tech.demeterapi.dao.entity.DemeterAuthHistoryExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

@Mapper
public interface DemeterAuthHistoryDao {

    public int countByExample(DemeterAuthHistoryExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(DemeterAuthHistory record);

    public int insertSelective(DemeterAuthHistory record);

    public List<DemeterAuthHistory> selectByExample(DemeterAuthHistoryExample example);

    public DemeterAuthHistory selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") DemeterAuthHistory record, @Param("example") DemeterAuthHistoryExample example);

    public int updateByExample(@Param("record") DemeterAuthHistory record, @Param("example") DemeterAuthHistoryExample example);

    public int updateByPrimaryKeySelective(DemeterAuthHistory record);

    public int updateByPrimaryKey(DemeterAuthHistory record);
}