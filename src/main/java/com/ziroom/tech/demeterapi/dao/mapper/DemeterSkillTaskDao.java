package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillTask;
import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillTaskExample;
import java.util.List;
import java.util.Map;

import com.ziroom.tech.demeterapi.dao.entity.ForTaskName;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

/**
 * <pre>
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑, 永无BUG!
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * </pre>
 */
@Repository
public interface DemeterSkillTaskDao {

    public List<ForTaskName> getTasksName(@Param("taskIds") List<Long> taskIds);

    public int countByExample(DemeterSkillTaskExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(DemeterSkillTask record);

    public int insertSelective(DemeterSkillTask record);

    public List<DemeterSkillTask> selectByExample(DemeterSkillTaskExample example);

    public DemeterSkillTask selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") DemeterSkillTask record, @Param("example") DemeterSkillTaskExample example);

    public int updateByExample(@Param("record") DemeterSkillTask record, @Param("example") DemeterSkillTaskExample example);

    public int updateByPrimaryKeySelective(DemeterSkillTask record);

    public int updateByPrimaryKey(DemeterSkillTask record);
}