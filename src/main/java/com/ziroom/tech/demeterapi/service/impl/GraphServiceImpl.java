package com.ziroom.tech.demeterapi.service.impl;

import com.ziroom.tech.demeterapi.dao.entity.GraphAreaSkill;
import com.ziroom.tech.demeterapi.dao.entity.GraphSkill;
import com.ziroom.tech.demeterapi.dao.entity.GraphSubSkillTask;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.Graph.GraphSkillReq;
import com.ziroom.tech.demeterapi.service.GraphService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GraphServiceImpl implements GraphService {
    @Override
    public Resp<Object> insertGraph(GraphSkill graphSkill) {
        return null;
    }
    @Override
    public Resp<Object> insertSkill(GraphAreaSkill graphAreaSkill) {
        return null;
    }
    @Override
    public Resp<Object> insertSubSkill(GraphSubSkillTask graphSubSkillTask) {
        return null;
    }
    @Override
    public Resp<List<GraphSkill>> listGraphSkill(GraphSkillReq graphSkillReq) {
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
