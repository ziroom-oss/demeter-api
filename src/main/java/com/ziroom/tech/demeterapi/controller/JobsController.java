package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.dao.entity.Jobs;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.service.JobsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api("职务查询")
@RestController
@Slf4j
@RequestMapping("/api/jobs")
public class JobsController {
    @Resource
    private JobsService jobsService;
    @GetMapping("/")
    public Resp<List<Jobs>> getAllJobs() {
        return Resp.success(jobsService.selectAll());
    }
    @GetMapping("/code/{code}")
    public Resp<Jobs> getJobsByCode(@PathVariable Integer code) {
        return Resp.success(jobsService.selectByCode(code));
    }
}
