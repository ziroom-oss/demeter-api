package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.dao.entity.SkillMap;
import com.ziroom.tech.demeterapi.po.dto.req.Map.SkillMapListReq;

import com.ziroom.tech.demeterapi.po.dto.resp.map.SummaryData;
import java.util.List;

public interface MapService {

    /**
     * 计算数量
     */
    Integer countBySkillMap(SkillMapListReq skillMapListReq);

    /**
     * 新建技术图谱
     */
    Long insertSelective(SkillMap record);

    /**
     * 删除指定 id 的图谱
     */
    Integer deleteByPrimaryKey(Long id);

    /**
     * 按查询条件返回技能图谱列表
     */
    List<SkillMap> selectByConditionSelective(SkillMapListReq skillMapListReq);

    /**
     * 修改指定技术图谱
     */
    Integer updateByPrimaryKeySelective(SkillMap record);

    /**
     * 返回指定 id 的技术图谱
     */
    SkillMap selectByPrimaryKey(Long id);

    /**
     * 技能图谱汇总数据-个人
     */
    SummaryData getSkillGraphData();

    //获取所有
    List<SkillMap> selectAll();
}
