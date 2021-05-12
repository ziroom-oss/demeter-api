package com.ziroom.tech.demeterapi.service.impl;

import com.ziroom.tech.demeterapi.dao.entity.GraphAreaSkill;
import com.ziroom.tech.demeterapi.dao.entity.GraphSkill;
import com.ziroom.tech.demeterapi.dao.entity.GraphSubSkillTask;
import com.ziroom.tech.demeterapi.dao.mapper.GraphAreaSkillDao;
import com.ziroom.tech.demeterapi.dao.mapper.GraphSkillDao;
import com.ziroom.tech.demeterapi.dao.mapper.GraphSubSkillTaskDao;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.Graph.GraphSkillListReq;
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

    @Override
    public Resp<Object> insertGraph(GraphSkill graphSkill) {
        return null;
    }
    @Override
    public Resp<Object> updateGraph(GraphSkill graphSkill) {
        return null;
    }
    @Override
    public Resp<Object> insertSkill(GraphAreaSkill graphAreaSkill) {
        return null;
    }
    @Override
    public Resp<Object> updateSkill(GraphAreaSkill graphAreaSkill) {
        return null;
    }
    @Override
    public Resp<Object> insertSubSkill(GraphSubSkillTask graphSubSkillTask) {
        return null;
    }
    @Override
    public Resp<Object> updateSubSkill(GraphSubSkillTask graphSubSkillTask) {
        return null;
    }
    @Override
    public Resp<List<GraphSkill>> listGraphSkill(GraphSkillListReq graphSkillListReq) {
        return null;
    }
    @Override
    public Resp<List<GraphAreaSkill>> listGraphAreaSkill(Long graphId) {
        return null;
    }
    @Override
    public Resp<List<GraphSubSkillTask>> listGraphSubSkillTask(Long skillId) {
        return null;
    };
}
