package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.GraphSkill;
import com.ziroom.tech.demeterapi.dao.entity.GraphSkillExample;
import java.util.List;

import com.ziroom.tech.demeterapi.po.dto.req.Graph.GraphSkillListReq;
import com.ziroom.tech.demeterapi.po.dto.req.Graph.GraphSkillReq;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

@SuppressWarnings("UnnecessaryInterfaceModifier")
@Repository
public interface GraphSkillDao {

    public int countByExample(GraphSkillExample example);

//    public int deleteByPrimaryKey(Long id);

    public int insert(GraphSkill record);

    public int insertSelective(GraphSkill record);

    /**
     * 删除指定 id 的图谱
     */
    public int deleteByPrimaryKey(Long id);

    public List<GraphSkill> selectByExample(GraphSkillExample example);

    /**
     * 不传参数默认返回所有数据
     */
    public List<GraphSkill> selectAll();

    /**
     * 按查询条件返回技能图谱列表
     */
    public List<GraphSkill> selectByCondition(@Param("graphSkillListReq") GraphSkillListReq graphSkillListReq);

    /**
     * 按 id 返回单个技能图谱
     */
    public GraphSkill selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") GraphSkill record, @Param("example") GraphSkillExample example);

    public int updateByExample(@Param("record") GraphSkill record, @Param("example") GraphSkillExample example);

    public int updateByPrimaryKeySelective(GraphSkill record);

    public int updateByPrimaryKey(GraphSkill record);
}