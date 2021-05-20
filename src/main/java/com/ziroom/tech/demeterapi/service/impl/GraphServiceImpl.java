package com.ziroom.tech.demeterapi.service.impl;

import com.ziroom.tech.demeterapi.dao.entity.DemeterPosition;
import com.ziroom.tech.demeterapi.dao.entity.GraphAreaSkill;
import com.ziroom.tech.demeterapi.dao.entity.GraphSkill;
import com.ziroom.tech.demeterapi.dao.entity.GraphSubSkillTask;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterPositionDao;
import com.ziroom.tech.demeterapi.dao.mapper.GraphAreaSkillDao;
import com.ziroom.tech.demeterapi.dao.mapper.GraphSkillDao;
import com.ziroom.tech.demeterapi.dao.mapper.GraphSubSkillTaskDao;
import com.ziroom.tech.demeterapi.po.dto.req.Graph.GraphSkillListReq;
import com.ziroom.tech.demeterapi.po.dto.req.Graph.GraphSkillReq;
import com.ziroom.tech.demeterapi.service.GraphService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class GraphServiceImpl implements GraphService {
    @Resource
    private GraphSkillDao graphSkillDao;
    @Resource
    private GraphAreaSkillDao graphAreaSkillDao;
    @Resource
    private GraphSubSkillTaskDao graphSubSkillTaskDao;
    @Resource
    private DemeterPositionDao demeterPositionDao;

    @Override
    public Long insertGraph(GraphSkill graphSkill) {
        graphSkillDao.insert(graphSkill);
        return graphSkill.getId();
    }
    @Override
    public int deleteGraph(Long id) {
        return graphSkillDao.deleteByPrimaryKey(id);
    }
    @Override
    public Long updateGraph(GraphSkill graphSkill) {
        graphSkillDao.updateByPrimaryKeySelective(graphSkill);
        return graphSkill.getId();
    }
    @Override
    public Long insertSkill(GraphAreaSkill graphAreaSkill) {
        graphAreaSkillDao.insertSelective(graphAreaSkill);
        return graphAreaSkill.getId();
    }
    @Override
    public Long updateSkill(GraphAreaSkill graphAreaSkill) {
        graphAreaSkillDao.updateByPrimaryKeySelective(graphAreaSkill);
        return graphAreaSkill.getId();
    }
    @Override
    public int insertSubSkill(GraphSubSkillTask graphSubSkillTask) {
        return graphSubSkillTaskDao.insert(graphSubSkillTask);
    }
    @Override
    public int updateSubSkill(GraphSubSkillTask graphSubSkillTask) {
        return graphSubSkillTaskDao.updateByPrimaryKeySelective(graphSubSkillTask);
    }

    @Override
    public List<GraphSkill> listAllGraphSkill() {
        return graphSkillDao.selectAll();
    }
    @Override
    public List<GraphSkill> listGraphSkillByCondition(GraphSkillListReq graphSkillListReq) {
        return graphSkillDao.selectByCondition(graphSkillListReq);
    }
    @Override
    public GraphSkill getGraphSkill(Long id) {
        return graphSkillDao.selectByPrimaryKey(id);
    }

    @Override
    public List<GraphAreaSkill> listGraphAreaSkill(Long graphId) {
        return graphAreaSkillDao.selectByGraphId(graphId);
    }
    @Override
    public List<GraphSubSkillTask> listGraphSubSkillTask(Long skillId) {
        return graphSubSkillTaskDao.selectBySkillId(skillId);
    };
    @Override
    public List<DemeterPosition> listAllDemeterPosition() {
        return demeterPositionDao.selectAll();
    };
}
