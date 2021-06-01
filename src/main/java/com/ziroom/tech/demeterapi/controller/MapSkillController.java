package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.dao.entity.SkillMapSkill;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.MapSkill.MapSkillCreateReq;
import com.ziroom.tech.demeterapi.service.SkillMapSkillService;
import com.ziroom.tech.demeterapi.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@Api("查询图谱和技能点关联")
@RequestMapping("/map/skill")
public class MapSkillController {
    @Resource
    private SkillMapSkillService skillMapSkillService;

    @PostMapping("/{mapId}/skills")
    @ApiOperation("按 mapId 查询关联记录")
    public Resp<List<SkillMapSkill>> getMapSkills(@PathVariable Integer mapId) {
        // 按 mapId 返回所有的关联
        return Resp.success(skillMapSkillService.selectByMapId(mapId));
    }

    @PostMapping("/")
    @ApiOperation("创建图谱和技能点的关联")
    public Resp<Integer> createMapSkill(@RequestBody MapSkillCreateReq mapSkillCreateReq) {
        return Resp.success(skillMapSkillService.insertSelective(mapSkillCreateReq.getEntity()));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("移除指定 id 的记录")
    public Resp<Integer> deleteMapSkill(@PathVariable Long id) {
        return Resp.success(skillMapSkillService.deleteByPrimaryKey(id));
    }

    @GetMapping("/{id}")
    @ApiOperation("按 id 查询记录")
    public Resp<SkillMapSkill> getMapSkill(@PathVariable Long id) {
        return Resp.success(skillMapSkillService.selectByPrimaryKey(id));
    }
}
