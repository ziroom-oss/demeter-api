package com.ziroom.tech.demeterapi.service.impl;

import com.ziroom.tech.demeterapi.dao.entity.Jobs;
import com.ziroom.tech.demeterapi.dao.mapper.JobsDao;
import com.ziroom.tech.demeterapi.po.dto.req.jobs.JobsCreateReq;
import com.ziroom.tech.demeterapi.po.dto.req.jobs.JobsModReq;
import com.ziroom.tech.demeterapi.service.JobsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class JobsServiceImpl implements JobsService {
    @Resource
    private JobsDao jobsDao;
    @Override
    public List<Jobs> selectAll() {
        return jobsDao.selectAll();
    }

    @Override
    public Jobs selectByCode(Integer code) {
        return jobsDao.selectByCode(code);
    }

    @Override
    public Integer deleteByPrimaryKey(Long id) {
        return jobsDao.deleteByPrimaryKey(id);
    }

    @Override
    public Integer updateByPrimaryKey(JobsModReq jobsModReq) {
        return jobsDao.updateByPrimaryKeySelective(jobsModReq);
    }

    @Override
    public Long insertSelective(JobsCreateReq jobsCreateReq) {
        jobsDao.insertSelective(jobsCreateReq);
        return jobsCreateReq.getId();
    }
}
