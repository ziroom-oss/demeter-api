package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.GraphSkill;
import com.ziroom.tech.demeterapi.dao.entity.GraphSkillExample;
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
public interface GraphSkillDao {

    public int countByExample(GraphSkillExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(GraphSkill record);

    public int insertSelective(GraphSkill record);

    public List<GraphSkill> selectByExample(GraphSkillExample example);

    public GraphSkill selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") GraphSkill record, @Param("example") GraphSkillExample example);

    public int updateByExample(@Param("record") GraphSkill record, @Param("example") GraphSkillExample example);

    public int updateByPrimaryKeySelective(GraphSkill record);

    public int updateByPrimaryKey(GraphSkill record);
}