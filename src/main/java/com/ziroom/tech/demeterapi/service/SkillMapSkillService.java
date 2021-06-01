package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.dao.entity.SkillMapSkill;

import java.util.List;

public interface SkillMapSkillService {
    public Integer insertSelective(SkillMapSkill skillMapSkill);
    public Integer deleteByPrimaryKey(Long id);
    public SkillMapSkill selectByPrimaryKey(Long id);
    public List<SkillMapSkill> selectByMapId(Integer skillMapId);
}
