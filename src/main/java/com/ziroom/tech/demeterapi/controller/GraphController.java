package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.common.enums.TechRanks;
import com.ziroom.tech.demeterapi.dao.entity.DemeterPosition;
import com.ziroom.tech.demeterapi.dao.entity.GraphAreaSkill;
import com.ziroom.tech.demeterapi.dao.entity.GraphSkill;
import com.ziroom.tech.demeterapi.dao.entity.GraphSubSkillTask;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.Graph.GraphAreaSkillReq;
import com.ziroom.tech.demeterapi.po.dto.req.Graph.GraphSkillListReq;
import com.ziroom.tech.demeterapi.po.dto.req.Graph.GraphSkillReq;
import com.ziroom.tech.demeterapi.po.dto.req.Graph.GraphSubSkillTaskReq;
import com.ziroom.tech.demeterapi.po.dto.resp.enums.TechRanksEnum;
import com.ziroom.tech.demeterapi.service.GraphService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    public Resp<List<GraphAreaSkill>> listGraphArea(@RequestParam Long graphId) {
        return Resp.success(graphService.listGraphAreaSkill(graphId));
    }

    /**
     * 获取子技能列表
     */
    @GetMapping("/subSkill/list")
    public Resp<List<GraphSubSkillTask>> listSubSkill(@RequestParam Long skillId) {
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

    @DeleteMapping("/")
    public Resp<Object> deleteGraph(@RequestParam Long id) {
        return Resp.success(graphService.deleteGraph(id));
    }

    @DeleteMapping("/area")
    public Resp<Object> deleteArea(@RequestParam Long id) {
        return Resp.success(graphService.deleteAreaSkill(id));
    }

    @DeleteMapping("/subSkill")
    public Resp<Object> deleteSubSkill(@RequestParam Long id) {
        return Resp.success(graphService.deleteSubSkill(id));
    }

    /**
     * 创建和修改技能领域
     */
    @PostMapping("/area")
    public Resp<Object> createArea(@RequestBody GraphAreaSkillReq graphAreaSkillReq) {
        GraphAreaSkill graphAreaSkill = graphAreaSkillReq.getEntity();
        if (Objects.nonNull(graphAreaSkill.getId())) {
           return Resp.success(graphService.updateAreaSkill(graphAreaSkill));
        } else {
            return Resp.success(graphService.insertAreaSkill(graphAreaSkill));
        }
    }

    /**
     * 创建和修改子技能
     */
    @PostMapping("/subSkill")
    public Resp<Object> createSubSkill(@RequestBody GraphSubSkillTaskReq graphSubSkillTaskReq) {
        GraphSubSkillTask graphSubSkillTask = graphSubSkillTaskReq.getEntity();
        if (Objects.nonNull(graphSubSkillTask.getId())) {
            return Resp.success(graphService.updateSubSkill(graphSubSkillTask));
        } else {
            return Resp.success(graphService.insertSubSkill(graphSubSkillTask));
        }
    }

    /**
     * 获取 T 序列
     */
    @GetMapping("/tech/ranks")
    public Resp<List<TechRanksEnum>> listTechRanks() {
        List<TechRanksEnum> techRanksEnumList = new ArrayList<>();
        for (TechRanks techRanks : TechRanks.values()) {
            TechRanksEnum techRanksEnum = new TechRanksEnum();
            techRanksEnum.setCode(techRanks.getCode());
            techRanksEnum.setName(techRanks.getDesc());
            techRanksEnumList.add(techRanksEnum);
        }
        return Resp.success(techRanksEnumList);
    }

    /**
     * 查询所有职务
     */
    @GetMapping("/positions")
    public Resp<List<DemeterPosition>> listDemeterPosition() {
        return Resp.success(graphService.listAllDemeterPosition());
    }
}
