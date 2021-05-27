package com.ziroom.tech.demeterapi.service.impl;

import com.ziroom.tech.demeterapi.dao.entity.SkillTree;
import com.ziroom.tech.demeterapi.dao.mapper.SkillTreeDao;
import com.ziroom.tech.demeterapi.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class TreeServiceImpl implements TreeService {
    @Resource
    private SkillTreeDao skillTreeDao;
    @Override
    public List<SkillTree> selectByParentId(Integer parentId) {
        return skillTreeDao.selectByParentId(parentId);
    }

    @Override
    public Integer deleteByPrimaryKey(Integer id) {
        return skillTreeDao.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insertSelective(SkillTree skillTree) {
        return skillTreeDao.insertSelective(skillTree);
    }

    @Override
    public SkillTree selectByPrimaryKey(Integer id) {
        return skillTreeDao.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKeySelective(SkillTree skillTree) {
        return skillTreeDao.updateByPrimaryKeySelective(skillTree);
    }
}
