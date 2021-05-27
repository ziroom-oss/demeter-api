package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.dao.entity.SkillTree;

import java.util.List;

public interface TreeService {
    /**
     * 按 parentId 查询该节点下属的节点列表
     */
    public List<SkillTree> selectByParentId(Integer parentId);
    /**
     * 移除指定 id 的节点
     */
    public Integer deleteByPrimaryKey(Integer id);
    /**
     * 插入新的节点
     */
    public Integer insertSelective(SkillTree skillTree);
    /**
     * 按 id 查询节点
     */
    public SkillTree selectByPrimaryKey(Integer id);
    /**
     * 按 id 更新节点
     */
    public Integer updateByPrimaryKeySelective(SkillTree skillTree);
}
