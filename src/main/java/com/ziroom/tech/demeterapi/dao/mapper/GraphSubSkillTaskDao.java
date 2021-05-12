package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.GraphSubSkillTask;
import com.ziroom.tech.demeterapi.dao.entity.GraphSubSkillTaskExample;
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
public interface GraphSubSkillTaskDao {

    public int countByExample(GraphSubSkillTaskExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(GraphSubSkillTask record);

    public int insertSelective(GraphSubSkillTask record);

    public List<GraphSubSkillTask> selectByExample(GraphSubSkillTaskExample example);

    public GraphSubSkillTask selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") GraphSubSkillTask record, @Param("example") GraphSubSkillTaskExample example);

    public int updateByExample(@Param("record") GraphSubSkillTask record, @Param("example") GraphSubSkillTaskExample example);

    public int updateByPrimaryKeySelective(GraphSubSkillTask record);

    public int updateByPrimaryKey(GraphSubSkillTask record);
}