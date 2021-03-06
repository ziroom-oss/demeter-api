package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillTask;
import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillTaskExample;
import java.util.List;
import java.util.Map;

import com.ziroom.tech.demeterapi.dao.entity.ForTaskName;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

@Mapper
public interface DemeterSkillTaskDao {

    public List<ForTaskName> getTasksName(@Param("taskIds") List<Long> taskIds);

    public int countByExample(DemeterSkillTaskExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(DemeterSkillTask record);

    public int insertSelective(DemeterSkillTask record);

    public List<DemeterSkillTask> selectByExample(DemeterSkillTaskExample example);

    public List<DemeterSkillTask> selectByTaskIds(@Param("taskIdList")List<Long> taskIdList);

    public DemeterSkillTask selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") DemeterSkillTask record, @Param("example") DemeterSkillTaskExample example);

    public int updateByExample(@Param("record") DemeterSkillTask record, @Param("example") DemeterSkillTaskExample example);

    public int updateByPrimaryKeySelective(DemeterSkillTask record);

    public int updateByPrimaryKey(DemeterSkillTask record);
}