package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.GraphSubSkillTask;
import com.ziroom.tech.demeterapi.dao.entity.GraphSubSkillTaskExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

@SuppressWarnings("UnnecessaryInterfaceModifier")
@Repository
public interface GraphSubSkillTaskDao {

    public int countByExample(GraphSubSkillTaskExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(GraphSubSkillTask record);

    public int insertSelective(GraphSubSkillTask record);

    public List<GraphSubSkillTask> selectByExample(GraphSubSkillTaskExample example);

    public GraphSubSkillTask selectByPrimaryKey(Long id);

    /**
     * 按父级技能 id 查询子技能列表
     */
    public List<GraphSubSkillTask> selectBySkillId(Long skillId);

    public int updateByExampleSelective(@Param("record") GraphSubSkillTask record, @Param("example") GraphSubSkillTaskExample example);

    public int updateByExample(@Param("record") GraphSubSkillTask record, @Param("example") GraphSubSkillTaskExample example);

    public int updateByPrimaryKeySelective(GraphSubSkillTask record);

    public int updateByPrimaryKey(GraphSubSkillTask record);
}