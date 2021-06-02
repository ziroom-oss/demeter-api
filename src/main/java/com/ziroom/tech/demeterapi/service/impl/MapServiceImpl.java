package com.ziroom.tech.demeterapi.service.impl;

import com.ziroom.tech.demeterapi.dao.entity.SkillMap;
import com.ziroom.tech.demeterapi.dao.mapper.SkillMapDao;
import com.ziroom.tech.demeterapi.po.dto.req.Map.SkillMapListReq;
import com.ziroom.tech.demeterapi.service.MapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class MapServiceImpl implements MapService {
    @Resource
    private SkillMapDao skillMapDao;
    @Override
    public Integer countBySkillMap(SkillMapListReq skillMapListReq) {
        return skillMapDao.countBySkillMap(skillMapListReq);
    }
    @Override
    public Long insertSelective(SkillMap skillMap) {
        skillMapDao.insertSelective(skillMap);
        return skillMap.getId();
    }
    @Override
    public Integer deleteByPrimaryKey(Long id) {
        return skillMapDao.deleteByPrimaryKey(id);
    }
    @Override
    public List<SkillMap> selectByConditionSelective(SkillMapListReq skillMapListReq) {
        return skillMapDao.selectByConditionSelective(skillMapListReq);
    }
    @Override
    public Integer updateByPrimaryKeySelective(SkillMap skillMap) {
        return skillMapDao.updateByPrimaryKeySelective(skillMap);
    }
    @Override
    public SkillMap selectByPrimaryKey(Long id) {
        return skillMapDao.selectByPrimaryKey(id);
    }
}
