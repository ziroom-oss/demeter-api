package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.dao.entity.SkillMapSkill;

import java.util.List;

public interface SkillMapSkillService {
    Integer insertSelective(SkillMapSkill skillMapSkill);
    Integer deleteByPrimaryKey(Long id);
    SkillMapSkill selectByPrimaryKey(Long id);
    List<SkillMapSkill> selectByMapId(Integer skillMapId);
    Integer updateByPrimaryKeySelective(SkillMapSkill skillMapSkill);
}
