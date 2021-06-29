package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.SkillMapSkillBk;
import com.ziroom.tech.demeterapi.dao.entity.SkillMapSkillBkExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

/**
 * <pre>
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑, 永无BUG!
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * </pre>
 */
@SuppressWarnings("UnnecessaryInterfaceModifier")
@Repository
public interface SkillMapSkillBkDao {

    public int countByExample(SkillMapSkillBkExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(SkillMapSkillBk record);

    public int insertSelective(SkillMapSkillBk record);

    public List<SkillMapSkillBk> selectByExample(SkillMapSkillBkExample example);

    public SkillMapSkillBk selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") SkillMapSkillBk record, @Param("example") SkillMapSkillBkExample example);

    public int updateByExample(@Param("record") SkillMapSkillBk record, @Param("example") SkillMapSkillBkExample example);

    public int updateByPrimaryKeySelective(SkillMapSkillBk record);

    public int updateByPrimaryKey(SkillMapSkillBk record);
}