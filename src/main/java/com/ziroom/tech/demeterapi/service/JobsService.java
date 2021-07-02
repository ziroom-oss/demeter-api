package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.dao.entity.Jobs;
import com.ziroom.tech.demeterapi.po.dto.req.jobs.JobsCreateReq;
import com.ziroom.tech.demeterapi.po.dto.req.jobs.JobsModReq;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface JobsService {
    /**
     * 查询所有职务
     */
    public List<Jobs> selectAll();

    /**
     * 按职务代号查询职务
     */
    public Jobs selectByCode(Integer code);

    /**
     * 移除指定 id 的职务
     */
    public Integer deleteByPrimaryKey(Long id);

    /**
     * 更新 jobs
     */
    public Integer updateByPrimaryKey(JobsModReq jobsModReq);

    /**
     * 新建 jobs
     */
    public Long insertSelective(JobsCreateReq jobsCreateReq);
}
