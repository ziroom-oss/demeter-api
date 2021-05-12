package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.dao.entity.GraphAreaSkill;
import com.ziroom.tech.demeterapi.dao.entity.GraphSkill;
import com.ziroom.tech.demeterapi.dao.entity.GraphSubSkillTask;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.Graph.GraphSkillReq;
import org.springframework.stereotype.Service;

import java.util.List;
public interface GraphService {
    /**
     * 创建技能图谱
     */
    Resp<Object> insertGraph(GraphSkill graphSkill);

    /**
     * 更新技能图谱
     */
    Resp<Object> updateGraph(GraphSkill graphSkill);

    /**
     * 创建技能领域
     * 每一个技能领域至少包含一个技能
     */
    Resp<Object> insertSkill(GraphAreaSkill graphAreaSkill);

    /**
     * 更新技能领域
     */
    Resp<Object> updateSkill(GraphAreaSkill graphAreaSkill);

    /**
     * 创建子技能
     * 每一个子技能必须与一个任务关联
     */
    Resp<Object> insertSubSkill(GraphSubSkillTask graphSubSkillTask);

    /**
     * 更新子技能
     */
    Resp<Object> updateSubSkill(GraphSubSkillTask graphSubSkillTask);

    /**
     * 查询技能图谱列表
     */
    Resp<List<GraphSkill>> listGraphSkill(GraphSkillReq graphSkillReq);

    /**
     * 查询技能领域列表
     * @param graphId 技能图谱ID
     */
    Resp<List<GraphAreaSkill>> listGraphAreaSkill(Long graphId);

    /**
     * 查询子技能列表
     * @param skillId 技能ID
     */
    Resp<List<GraphSubSkillTask>> listGraphSubSkillTask(Long skillId);
}
