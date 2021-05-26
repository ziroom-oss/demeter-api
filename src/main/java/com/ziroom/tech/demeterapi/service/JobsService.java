package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.dao.entity.Jobs;

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
}
