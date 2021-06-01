package com.ziroom.tech.demeterapi.service.impl;

import com.ziroom.tech.demeterapi.dao.entity.SkillMapSkill;
import com.ziroom.tech.demeterapi.dao.mapper.SkillMapSkillDao;
import com.ziroom.tech.demeterapi.service.SkillMapSkillService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SkillMapSkillServiceImpl implements SkillMapSkillService {
    @Resource
    private SkillMapSkillDao skillMapSkillDao;

    @Override
    public Integer deleteByPrimaryKey(Long id) {
        return skillMapSkillDao.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insertSelective(SkillMapSkill skillMapSkill) {
        return skillMapSkillDao.insertSelective(skillMapSkill);
    }

    @Override
    public SkillMapSkill selectByPrimaryKey(Long id) {
        return skillMapSkillDao.selectByPrimaryKey(id);
    }

    @Override
    public List<SkillMapSkill> selectByMapId(Integer skillMapId) {
        return skillMapSkillDao.selectByMapId(skillMapId);
    }

    @Override
    public Integer updateByPrimaryKeySelective(SkillMapSkill skillMapSkill) {
        return skillMapSkillDao.updateByPrimaryKeySelective(skillMapSkill);
    }
}
