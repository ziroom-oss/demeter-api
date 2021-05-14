package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.dao.entity.GraphAreaSkill;
import com.ziroom.tech.demeterapi.dao.entity.GraphSkill;
import com.ziroom.tech.demeterapi.dao.entity.GraphSubSkillTask;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.Graph.GraphSkillReq;
import com.ziroom.tech.demeterapi.service.GraphService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "任务相关")
@Slf4j
@RestController
@RequestMapping("api/graph")
public class GraphController {
    @Resource
    private GraphService graphService;

    /**
     * 获取图谱列表接口
     */
    @GetMapping("/list")
    public Resp<List<GraphSkill>> listGraphSkill() {
        return Resp.success();
    }

    /**
     * 获取技能领域列表
     * 技能领域同时包含它的技能
     */
    @GetMapping("/area/list")
    public Resp<List<GraphAreaSkill>> listGraphArea() {
        return Resp.success();
    }

    /**
     * 获取子技能列表
     */
    @GetMapping("/subSkill/list")
    public Resp<List<GraphSubSkillTask>> listSubSkill() {
        return Resp.success();
    }

    /**
     * 创建和修改技能图谱
     */
    @PostMapping("/")
    public Resp<Object> createGraph(GraphSkillReq graphSkillReq) {
        GraphSkill graphSkill = graphSkillReq.getEntity();
        return Resp.success(graphService.insertGraph(graphSkill));
    }

    /**
     * 创建和修改技能领域
     */
    @PostMapping("/area")
    public Resp<Object> createArea() {
        return Resp.success();
    }

    /**
     * 创建和修改子技能
     */
    @PostMapping("/subSkill")
    public Resp<Object> createSubSkill() {
        return Resp.success();
    }
}
