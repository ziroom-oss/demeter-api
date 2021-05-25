package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.common.PageListResp;
import com.ziroom.tech.demeterapi.dao.entity.SkillMap;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.Map.SkillMapCreateReq;
import com.ziroom.tech.demeterapi.po.dto.req.Map.SkillMapListReq;
import com.ziroom.tech.demeterapi.po.dto.req.Map.SkillMapModReq;
import com.ziroom.tech.demeterapi.service.MapService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api("技能图谱")
@Slf4j
@RestController
@RequestMapping("/api/map")
public class MapController {
    @Resource
    private MapService mapService;
    @PostMapping("/")
    public Resp<Byte> createMap(SkillMapCreateReq skillMapCreateReq) {
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
    public Resp<PageListResp<SkillMap>> selectByConditionSelective(SkillMapListReq skillMapListReq) {
        PageListResp<SkillMap> resp = PageListResp.emptyList();
        Integer total = mapService.countBySkillMap(skillMapListReq);
        if (total < NumberUtils.INTEGER_ONE) {
            return Resp.success(resp);
        }
        resp.setTotal(total);
        resp.setData(mapService.selectByConditionSelective(skillMapListReq));
        return Resp.success(resp);
    }
    @PostMapping("/{id}")
    public Resp<Byte> updateMap(SkillMapModReq skillMapModReq) {
        SkillMap skillMap = skillMapModReq.getEntity(skillMapModReq);
        return Resp.success(mapService.updateByPrimaryKeySelective(skillMap));
    }
}
