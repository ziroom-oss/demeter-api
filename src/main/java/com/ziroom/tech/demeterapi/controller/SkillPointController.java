package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.common.PageListResp;
import com.ziroom.tech.demeterapi.dao.entity.DemeterTaskUser;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.skill.BatchQueryReq;
import com.ziroom.tech.demeterapi.po.dto.req.skill.CheckSkillReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.SkillTaskReq;
import com.ziroom.tech.demeterapi.po.dto.resp.task.ReceiveQueryResp;
import com.ziroom.tech.demeterapi.po.dto.resp.task.SkillDetailResp;
import com.ziroom.tech.demeterapi.service.SkillPointService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author daijiankun
 */
@Slf4j
@RestController
@RequestMapping(value = "api/skill")
public class SkillPointController {

    @Resource
    private SkillPointService skillPointService;

    @PostMapping(value = "save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "新建技能点", httpMethod = "POST")
    public Resp<Object> createSkillTask(SkillTaskReq skillTaskReq) {
        skillTaskReq.validateAdd();
        return skillPointService.createSkillTask(skillTaskReq);
    }

    @PostMapping("get")
    @ApiOperation(value = "查看技能点", httpMethod = "POST")
    public Resp<SkillDetailResp> getSkillTask(@RequestParam Long id) {
        return skillPointService.getSkillTask(id);
    }


    @PostMapping(value = "update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "编辑技能点", httpMethod = "POST")
    public Resp<Object> updateSkillTask(SkillTaskReq skillTaskReq) {
        skillTaskReq.validateEdit();
        return skillPointService.updateSkillTask(skillTaskReq);
    }

    @PostMapping("/query")
    public Resp<Object> querySkillPoint(@RequestParam List<Integer> skillTreeId) {
        return Resp.success(skillPointService.querySkillPointFromTreeId(skillTreeId));
    }

    /**
     * batch query someone skill points information
     * @param batchQueryReq
     * @return
     */
    @PostMapping("/batchQuery")
    public Resp<List<DemeterTaskUser>> batchQuerySkillPoints(@RequestBody BatchQueryReq batchQueryReq) {
        return Resp.success(skillPointService.batchQuerySkillPoints(batchQueryReq));
    }

    /**
     * 技能点认证列表，查询当前登录人可以认证的技能点列表
     * @param checkSkillReq
     * @return
     */
    @PostMapping("/check/list")
    public Resp<PageListResp<ReceiveQueryResp>> getSkillPointsCheckList(@RequestBody CheckSkillReq checkSkillReq) {
        checkSkillReq.validate();
        return Resp.success(skillPointService.getSkillPointsCheckList(checkSkillReq));
    }

}
