package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.TaskFinishOutcome;
import com.ziroom.tech.demeterapi.dao.entity.TaskFinishOutcomeExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

@Mapper
public interface TaskFinishOutcomeDao {

    public int countByExample(TaskFinishOutcomeExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(TaskFinishOutcome record);

    public int insertSelective(TaskFinishOutcome record);

    public List<TaskFinishOutcome> selectByExample(TaskFinishOutcomeExample example);

    public TaskFinishOutcome selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") TaskFinishOutcome record, @Param("example") TaskFinishOutcomeExample example);

    public int updateByExample(@Param("record") TaskFinishOutcome record, @Param("example") TaskFinishOutcomeExample example);

    public int updateByPrimaryKeySelective(TaskFinishOutcome record);

    public int updateByPrimaryKey(TaskFinishOutcome record);

    int selectByTaskAndUid(@Param("taskId")Long taskId, @Param("receiveId")String receiveId);
}