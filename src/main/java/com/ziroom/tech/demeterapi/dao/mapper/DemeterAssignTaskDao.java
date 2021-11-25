package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.DemeterAssignTask;
import com.ziroom.tech.demeterapi.dao.entity.DemeterAssignTaskExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

@Mapper
public interface DemeterAssignTaskDao {

    public int countByExample(DemeterAssignTaskExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(DemeterAssignTask record);

    public int insertSelective(DemeterAssignTask record);

    public List<DemeterAssignTask> selectByExample(DemeterAssignTaskExample example);

    public DemeterAssignTask selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") DemeterAssignTask record, @Param("example") DemeterAssignTaskExample example);

    public int updateByExample(@Param("record") DemeterAssignTask record, @Param("example") DemeterAssignTaskExample example);

    public int updateByPrimaryKeySelective(DemeterAssignTask record);

    public int updateByPrimaryKey(DemeterAssignTask record);
}