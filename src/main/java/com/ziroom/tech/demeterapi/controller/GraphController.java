package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.dao.entity.GraphAreaSkill;
import com.ziroom.tech.demeterapi.dao.entity.GraphSkill;
import com.ziroom.tech.demeterapi.dao.entity.GraphSubSkillTask;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.Graph.GraphAreaSkillReq;
import com.ziroom.tech.demeterapi.po.dto.req.Graph.GraphSkillListReq;
import com.ziroom.tech.demeterapi.po.dto.req.Graph.GraphSkillReq;
import com.ziroom.tech.demeterapi.po.dto.req.Graph.GraphSubSkillTaskReq;
import com.ziroom.tech.demeterapi.service.GraphService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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
    @PostMapping("/list")
    public Resp<List<GraphSkill>> listGraphSkill(@RequestBody GraphSkillListReq graphSkillListReq) {
        return Resp.success(graphService.listGraphSkillByCondition(graphSkillListReq));
    }

    @GetMapping("/list/all")
    public Resp<List<GraphSkill>> listGraphSkillAll() {
        return Resp.success(graphService.listAllGraphSkill());
    }

    /**
     * 获取技能领域列表
     * 技能领域同时包含它的技能
     */
    @GetMapping("/area/list")
    public Resp<List<GraphAreaSkill>> listGraphArea(Long graphId) {
        return Resp.success(graphService.listGraphAreaSkill(graphId));
    }

    /**
     * 获取子技能列表
     */
    @GetMapping("/subSkill/list")
    public Resp<List<GraphSubSkillTask>> listSubSkill(Long skillId) {
        return Resp.success(graphService.listGraphSubSkillTask(skillId));
    }

    /**
     * 创建和修改技能图谱
     */
    @PostMapping("/")
    public Resp<Object> createGraph(@RequestBody GraphSkillReq graphSkillReq) {
        GraphSkill graphSkill = graphSkillReq.getEntity();
        if (Objects.nonNull(graphSkill.getId())) {
            return Resp.success(graphService.updateGraph(graphSkill));
        } else {
            return Resp.success(graphService.insertGraph(graphSkill));
        }
    }

    /**
     * 创建和修改技能领域
     */
    @PostMapping("/area")
    public Resp<Object> createArea(@RequestBody GraphAreaSkillReq graphAreaSkillReq) {
        GraphAreaSkill graphAreaSkill = graphAreaSkillReq.getEntity();
        return Resp.success(graphService.insertSkill(graphAreaSkill));
    }

    /**
     * 创建和修改子技能
     */
    @PostMapping("/subSkill")
    public Resp<Object> createSubSkill(@RequestBody GraphSubSkillTaskReq graphSubSkillTaskReq) {
        GraphSubSkillTask graphSubSkillTask = graphSubSkillTaskReq.getEntity();
        return Resp.success(graphService.insertSubSkill(graphSubSkillTask));
    }
}
