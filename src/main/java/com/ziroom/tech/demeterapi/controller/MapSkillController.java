package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.common.enums.TechRanks;
import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillTask;
import com.ziroom.tech.demeterapi.dao.entity.SkillMapSkill;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterSkillTaskDao;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.Map.SkillMapListReq;
import com.ziroom.tech.demeterapi.po.dto.req.MapSkill.MapSkillCreateReq;
import com.ziroom.tech.demeterapi.po.dto.req.MapSkill.MapSkillListReq;
import com.ziroom.tech.demeterapi.po.dto.req.MapSkill.MapSkillModReq;
import com.ziroom.tech.demeterapi.po.dto.resp.MapSkill.MapSkillResp;
import com.ziroom.tech.demeterapi.po.dto.resp.enums.TechRanksEnum;
import com.ziroom.tech.demeterapi.service.SkillMapSkillService;
import com.ziroom.tech.demeterapi.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@Api("查询图谱和技能点关联")
@RequestMapping("api/map/skill")
public class MapSkillController {
    @Resource
    private SkillMapSkillService skillMapSkillService;
    @Resource
    private DemeterSkillTaskDao demeterSkillTaskDao;

    @GetMapping("/{mapId}/tree")
    @ApiOperation("按 mapId 返回图形树")
    public Resp<List<MapSkillResp>> getMapSkillsTree(@PathVariable Integer mapId) {
        // 按 mapId 返回所有的关联
        List<SkillMapSkill> skillMapSkills = skillMapSkillService.selectByMapId(mapId);
        List<MapSkillResp> mapSkillResps = new ArrayList<>();
        Optional.ofNullable(skillMapSkills).ifPresent(records -> {
            records.forEach(record -> {
                MapSkillResp mapSkillResp = new MapSkillResp();
                BeanUtils.copyProperties(record, mapSkillResp);
                DemeterSkillTask demeterSkillTask = demeterSkillTaskDao.selectByPrimaryKey(record.getSkillTaskId());
                // skillTreeId 对应 demeterSkillTask.skillId
                mapSkillResp.setSkillTreeId(demeterSkillTask.getSkillId());
                mapSkillResp.setSkillLevel(demeterSkillTask.getSkillLevel());
                // 技能点名称对应 demeterSkillTask.taskName
                mapSkillResp.setSkillName(demeterSkillTask.getTaskName());
                mapSkillResp.setSkillReward(demeterSkillTask.getSkillReward());
                mapSkillResp.setPublisher(demeterSkillTask.getPublisher());
                mapSkillResps.add(mapSkillResp);
            });
        });
        return Resp.success(mapSkillResps);
    }

    @PostMapping("/{mapId}/skills")
    @ApiOperation("按 skillId 数组批量查询记录")
    public Resp<List<SkillMapSkill>> selectByCondition(@PathVariable Integer mapId, @RequestBody List<Long> skillTaskIds) {
        List<SkillMapSkill> skillMapSkills = new ArrayList<>();
        skillTaskIds.forEach(taskId -> {
            MapSkillListReq mapSkillListReq = new MapSkillListReq();
            mapSkillListReq.setSkillMapId(mapId);
            mapSkillListReq.setSkillTaskId(taskId);
            List<SkillMapSkill> skillMapSkills1 = skillMapSkillService.selectByConditionSelective(mapSkillListReq);
            skillMapSkills.addAll(skillMapSkills1);
        });
        return Resp.success(skillMapSkills);
    }

    @PostMapping("/")
    @ApiOperation("创建图谱和技能点的关联")
    public Resp<Integer> createMapSkill(@RequestBody MapSkillCreateReq mapSkillCreateReq) {
        return Resp.success(skillMapSkillService.insertSelective(mapSkillCreateReq.getEntity(mapSkillCreateReq)));
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

    @PatchMapping("/{id}")
    @ApiOperation("按 id 修改记录")
    public Resp<Integer> updateMapSkill(@PathVariable Long id, @RequestBody MapSkillModReq mapSkillModReq) {
        mapSkillModReq.setId(id);
        SkillMapSkill skillMapSkill = mapSkillModReq.getEntity(mapSkillModReq);
        return Resp.success(skillMapSkillService.updateByPrimaryKeySelective(skillMapSkill));
    }

    @PostMapping("/search")
    @ApiOperation(("按图谱 id 和技能 id 查询记录"))
    public Resp<List<SkillMapSkill>> getMapSkillsByCondition(@RequestBody MapSkillListReq mapSkillListReq) {
        return Resp.success(skillMapSkillService.selectByConditionSelective(mapSkillListReq));
    }

    @GetMapping("/job/levels")
    @ApiOperation("获取职级列表")
    public Resp<List<TechRanksEnum>> getTechRanks() {
        List<TechRanksEnum> techRanksEnumList = new ArrayList<>();
        for (TechRanks techRanks : TechRanks.values()) {
            TechRanksEnum techRanksEnum = new TechRanksEnum();
            techRanksEnum.setCode(techRanks.getCode());
            techRanksEnum.setName(techRanks.getDesc());
            techRanksEnumList.add(techRanksEnum);
        }
        return Resp.success(techRanksEnumList);
    }
}
