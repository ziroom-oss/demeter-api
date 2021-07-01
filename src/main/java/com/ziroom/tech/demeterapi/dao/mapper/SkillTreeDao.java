package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.SkillTree;
import java.util.List;

import org.springframework.stereotype.Repository;

@SuppressWarnings("UnnecessaryInterfaceModifier")
@Repository
public interface SkillTreeDao {
    public List<SkillTree> selectByParentId(Integer parentId);

    public Integer deleteByPrimaryKey(Integer id);

    public Integer insertSelective(SkillTree record);

    public SkillTree selectByPrimaryKey(Integer id);

    public Integer updateByPrimaryKeySelective(SkillTree record);

    public List<SkillTree> selectAll();
}