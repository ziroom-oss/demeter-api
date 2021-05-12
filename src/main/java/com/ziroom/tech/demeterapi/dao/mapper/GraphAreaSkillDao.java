package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.GraphAreaSkill;
import com.ziroom.tech.demeterapi.dao.entity.GraphAreaSkillExample;
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
public interface GraphAreaSkillDao {

    public int countByExample(GraphAreaSkillExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(GraphAreaSkill record);

    public int insertSelective(GraphAreaSkill record);

    public List<GraphAreaSkill> selectByExample(GraphAreaSkillExample example);

    public GraphAreaSkill selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") GraphAreaSkill record, @Param("example") GraphAreaSkillExample example);

    public int updateByExample(@Param("record") GraphAreaSkill record, @Param("example") GraphAreaSkillExample example);

    public int updateByPrimaryKeySelective(GraphAreaSkill record);

    public int updateByPrimaryKey(GraphAreaSkill record);
}