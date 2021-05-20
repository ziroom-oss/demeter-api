package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.dao.entity.DemeterPosition;
import com.ziroom.tech.demeterapi.dao.entity.GraphAreaSkill;
import com.ziroom.tech.demeterapi.dao.entity.GraphSkill;
import com.ziroom.tech.demeterapi.dao.entity.GraphSubSkillTask;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.Graph.GraphSkillListReq;

import java.util.List;
public interface GraphService {
    /**
     * 创建技能图谱
     */
    int insertGraph(GraphSkill graphSkill);

    /**
     * 更新技能图谱
     */
    int updateGraph(GraphSkill graphSkill);

    /**
     * 创建技能领域
     * 每一个技能领域至少包含一个技能
     */
    Long insertSkill(GraphAreaSkill graphAreaSkill);

    /**
     * 更新技能领域
     */
    Long updateSkill(GraphAreaSkill graphAreaSkill);

    /**
     * 创建子技能
     * 每一个子技能必须与一个任务关联
     */
    int insertSubSkill(GraphSubSkillTask graphSubSkillTask);

    /**
     * 更新子技能
     */
    int updateSubSkill(GraphSubSkillTask graphSubSkillTask);

    /**
     * 按条件查询技能图谱列表
     */
    List<GraphSkill> listGraphSkillByCondition(GraphSkillListReq graphSkillListReq);

    /**
     * 按 id 查询技能图谱
     */
    GraphSkill getGraphSkill(Long id);

    /**
     * 返回所有技能图谱
     */
    List<GraphSkill> listAllGraphSkill();

    /**
     * 查询技能领域列表
     * @param graphId 技能图谱ID
     */
    List<GraphAreaSkill> listGraphAreaSkill(Long graphId);

    /**
     * 查询子技能列表
     * @param skillId （父级）技能ID
     */
    List<GraphSubSkillTask> listGraphSubSkillTask(Long skillId);

    /**
     * 查询所有职务
     */
    List<DemeterPosition> listAllDemeterPosition();
}
