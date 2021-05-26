package com.ziroom.tech.demeterapi.controller;

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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Api("技能图谱")
@Slf4j
@RestController
@RequestMapping("/api/map")
public class MapController {
    @Resource
    private MapService mapService;
    @Resource
    private JobsService jobsService;

    @PostMapping("/")
    public Resp<Byte> createMap(@RequestBody SkillMapCreateReq skillMapCreateReq) {
        return Resp.success(mapService.insertSelective(skillMapCreateReq.getEntity()));
    }

    @DeleteMapping("/{id}")
    public Resp<Byte> deleteMap(@PathVariable Long id) {
        return Resp.success(mapService.deleteByPrimaryKey(id));
    }

    @GetMapping("/{id}")
    public Resp<SkillMap> getMap(@PathVariable Long id) {
        return Resp.success(mapService.selectByPrimaryKey(id));
    }

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
            Optional<String> jobName = Optional.ofNullable(
                    jobsService.selectByCode(skillMap.getJobId()).getName()
            );
            skillMapResp.setJobName(jobName);
            skillMapResps.add(skillMapResp);
        });
        resp.setData(skillMapResps);
        return Resp.success(resp);
    }

    @PostMapping("/{id}")
    public Resp<Byte> updateMap(@PathVariable Long id, @RequestBody SkillMapModReq skillMapModReq) {
        SkillMap skillMap = skillMapModReq.getEntity(skillMapModReq);
        skillMap.setId(id);
        return Resp.success(mapService.updateByPrimaryKeySelective(skillMap));
    }
}
