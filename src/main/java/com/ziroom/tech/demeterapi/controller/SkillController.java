package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.service.SkillPointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping(value = "/api/skill")
public class SkillController {

    @Resource
    private SkillPointService skillPointService;

    @PostMapping("/query")
    public Resp<Object> querySkillPoint(@RequestParam List<Long> skillTreeId) {
        return Resp.success(skillPointService.querySkillPointFromTreeId(skillTreeId));
    }

}
