package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.TaskFinishConditionInfo;
import com.ziroom.tech.demeterapi.dao.entity.TaskFinishConditionInfoExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

@Mapper
public interface TaskFinishConditionInfoDao {

    public int countByExample(TaskFinishConditionInfoExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(TaskFinishConditionInfo record);

    public int insertSelective(TaskFinishConditionInfo record);

    public List<TaskFinishConditionInfo> selectByExample(TaskFinishConditionInfoExample example);

    public TaskFinishConditionInfo selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") TaskFinishConditionInfo record, @Param("example") TaskFinishConditionInfoExample example);

    public int updateByExample(@Param("record") TaskFinishConditionInfo record, @Param("example") TaskFinishConditionInfoExample example);

    public int updateByPrimaryKeySelective(TaskFinishConditionInfo record);

    public int updateByPrimaryKey(TaskFinishConditionInfo record);

    int selectByTaskAndUid(@Param("taskId")Long taskId, @Param("receiveId")String receiveId);
}