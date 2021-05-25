package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.SkillMap;

import java.util.List;

import com.ziroom.tech.demeterapi.po.dto.req.Map.SkillMapListReq;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;
@SuppressWarnings("UnnecessaryInterfaceModifier")
@Repository
public interface SkillMapDao {

    /**
     * 计算数量
     */
    public Integer countBySkillMap(SkillMapListReq record);

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
    public List<SkillMap> selectByConditionSelective(@Param("skillMapListReq") SkillMapListReq skillMapListReq);

    /**
     * 修改指定技术图谱
     */
    public Byte updateByPrimaryKeySelective(SkillMap record);

    /**
     * 返回指定 id 的技术图谱
     */
    public SkillMap selectByPrimaryKey(Long id);
}