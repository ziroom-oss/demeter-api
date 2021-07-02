package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.SkillMapSkill;

import com.ziroom.tech.demeterapi.po.dto.req.MapSkill.MapSkillListReq;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("UnnecessaryInterfaceModifier")
@Repository
public interface SkillMapSkillDao {

    /**
     * 移除指定 id 的表
     * @param id
     * @return
     */
    public Integer deleteByPrimaryKey(Long id);

    /**
     * 新建图谱和技能点的关联
     * @param record
     * @return
     */
    public Integer insertSelective(SkillMapSkill record);

    /**
     * 按 id 查询数据
     * @param id
     * @return
     */
    public SkillMapSkill selectByPrimaryKey(Long id);

    /**
     * 按 skillMapId 查询数据
     * @param skillMapId 技能图谱 id
     * @return
     */
    public List<SkillMapSkill> selectByMapId(Integer skillMapId);

    /**
     * 按 id 更新数据
     * @param skillMapSkill
     * @return
     */
    public Integer updateByPrimaryKeySelective(SkillMapSkill skillMapSkill);

    /**
     * 按查询条件返回记录
     */
    public List<SkillMapSkill> selectByConditionSelective(@Param("mapSkillListReq") MapSkillListReq mapSkillListReq);
}