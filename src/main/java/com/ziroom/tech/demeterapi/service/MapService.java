package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.dao.entity.SkillMap;
import com.ziroom.tech.demeterapi.po.dto.req.Map.SkillMapListReq;

import java.util.List;

public interface MapService {

    /**
     * 计算数量
     */
    public Integer countBySkillMap(SkillMapListReq skillMapListReq);

    /**
     * 新建技术图谱
     */
    public Byte insertSelective(SkillMap record);

    /**
     * 删除指定 id 的图谱
     */
    public Byte deleteByPrimaryKey(Long id);

    /**
     * 按查询条件返回技能图谱列表
     */
    public List<SkillMap> selectByConditionSelective(SkillMapListReq skillMapListReq);

    /**
     * 修改指定技术图谱
     */
    public Byte updateByPrimaryKeySelective(SkillMap record);

    /**
     * 返回指定 id 的技术图谱
     */
    public SkillMap selectByPrimaryKey(Long id);
}
