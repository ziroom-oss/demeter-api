package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.SkillTree;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
public interface SkillTreeDao {

    public Map<Integer, String> getSkillsName(@Param("parentIds") List<Long> parentIds);

    public List<SkillTree> selectByParentId(Integer parentId);

    public Integer deleteByPrimaryKey(Integer id);

    public Integer insertSelective(SkillTree record);

    public SkillTree selectByPrimaryKey(Integer id);

    public Integer updateByPrimaryKeySelective(SkillTree record);

    public List<SkillTree> selectAll();
}