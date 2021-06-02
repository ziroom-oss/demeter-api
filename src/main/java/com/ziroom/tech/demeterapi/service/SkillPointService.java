package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillTask;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.task.SkillTaskReq;
import com.ziroom.tech.demeterapi.po.dto.resp.task.SkillDetailResp;

import java.util.List;
import java.util.Map;

/**
 * @author daijiankun
 */
public interface SkillPointService {


    /**
     * 创建技能点
     * @param skillTaskReq 技能点请求体
     * @return Resp
     */
    Resp<Object> createSkillTask(SkillTaskReq skillTaskReq);

    Resp<SkillDetailResp> getSkillTask(Long id);

    /**
     * 编辑技能点
     * @param skillTaskReq  技能点请求体
     * @return Resp
     */
    Resp<Object> updateSkillTask(SkillTaskReq skillTaskReq);

    Map<Integer, List<DemeterSkillTask>> querySkillPointFromTreeId(List<Integer> skillTreeId);
}
