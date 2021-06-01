package com.ziroom.tech.demeterapi.controller;

import com.google.common.base.Preconditions;
import com.ziroom.tech.demeterapi.common.PageListResp;
import com.ziroom.tech.demeterapi.dao.entity.Jobs;
import com.ziroom.tech.demeterapi.dao.entity.SkillMap;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.Map.SkillMapCreateReq;
import com.ziroom.tech.demeterapi.po.dto.req.Map.SkillMapListReq;
import com.ziroom.tech.demeterapi.po.dto.req.Map.SkillMapModReq;
import com.ziroom.tech.demeterapi.po.dto.resp.map.SkillMapResp;
import com.ziroom.tech.demeterapi.service.JobsService;
import com.ziroom.tech.demeterapi.service.MapService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Api("技能图谱")
@Slf4j
@RestController
@RequestMapping("/api/map")
public class MapController {
    @Resource
    private MapService mapService;
    @Resource
    private JobsService jobsService;

    @ApiOperation("创建技能图谱")
    @PostMapping("/")
    public Resp<Long> createMap(@RequestBody SkillMapCreateReq skillMapCreateReq) {
        return Resp.success(mapService.insertSelective(skillMapCreateReq.getEntity()));
    }

    @ApiOperation("移除指定 id 的技能图谱")
    @DeleteMapping("/{id}")
    public Resp<Integer> deleteMap(@PathVariable Long id) {
        Preconditions.checkArgument(Objects.nonNull(id), "删除技能图谱的 id 不能为空");
        return Resp.success(mapService.deleteByPrimaryKey(id));
    }

    @ApiOperation("获取指定 id 的技能图谱")
    @GetMapping("/{id}")
    public Resp<SkillMap> getMap(@PathVariable Long id) {
        return Resp.success(mapService.selectByPrimaryKey(id));
    }

    @ApiOperation("按条件查询技能图谱")
    @PostMapping("/condition")
    public Resp<PageListResp<SkillMapResp>> selectByConditionSelective(@RequestBody SkillMapListReq skillMapListReq) {
        skillMapListReq.validate();
        PageListResp<SkillMapResp> resp = PageListResp.emptyList();
        Integer total = mapService.countBySkillMap(skillMapListReq);
        if (total < NumberUtils.INTEGER_ONE) {
            return Resp.success(resp);
        }
        resp.setTotal(total);
        List<SkillMap> skillMaps = mapService.selectByConditionSelective(skillMapListReq);
        List<SkillMapResp> skillMapResps = new ArrayList<>();
        skillMaps.forEach(skillMap -> {
            SkillMapResp skillMapResp = new SkillMapResp();
            BeanUtils.copyProperties(skillMap, skillMapResp);
            // skillMap.jobId 是 jobs 表的主键
            Jobs jobs = jobsService.selectByCode(skillMap.getJobId());
            String jobName = "";
            if (Objects.nonNull(jobs)) {
                jobName = jobs.getName();
            }
            skillMapResp.setJobName(jobName);
            skillMapResps.add(skillMapResp);
        });
        resp.setData(skillMapResps);
        return Resp.success(resp);
    }

    @ApiOperation("按 id 更新技能图谱")
    @PatchMapping("/{id}")
    public Resp<Integer> updateMap(@PathVariable Long id, @RequestBody SkillMapModReq skillMapModReq) {
        skillMapModReq.setId(id);
        SkillMap skillMap = skillMapModReq.getEntity(skillMapModReq);
        return Resp.success(mapService.updateByPrimaryKeySelective(skillMap));
    }
}
