package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.common.exception.BusinessException;
import com.ziroom.tech.demeterapi.dao.entity.Jobs;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.jobs.JobsCreateReq;
import com.ziroom.tech.demeterapi.po.dto.req.jobs.JobsModReq;
import com.ziroom.tech.demeterapi.service.JobsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{id}")
    public Resp<Integer> delete(@PathVariable Long id) {
        return Resp.success(jobsService.deleteByPrimaryKey(id));
    }

    @PutMapping("/{id}")
    public Resp<Integer> update(@PathVariable Long id, @RequestBody JobsModReq jobsModReq) {
        jobsModReq.setId(id);
        return Resp.success(jobsService.updateByPrimaryKey(jobsModReq));
    }

    @PostMapping("/")
    public Resp<Long> create(@RequestBody JobsCreateReq jobsCreateReq) {
        jobsCreateReq.validate();
        Long id = null;
        try {
            id = jobsService.insertSelective(jobsCreateReq);
        } catch (Exception exception) {
            if (exception instanceof DuplicateKeyException) {
                throw new BusinessException(jobsCreateReq.getCode() + " 已存在，请勿重复提交");
            }
        }
        return Resp.success(id);
    }
}
