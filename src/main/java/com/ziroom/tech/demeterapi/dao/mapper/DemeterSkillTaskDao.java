package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillTask;
import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillTaskExample;
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
public interface DemeterSkillTaskDao {

    public int countByExample(DemeterSkillTaskExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(DemeterSkillTask record);

    public int insertSelective(DemeterSkillTask record);

    public List<DemeterSkillTask> selectByExample(DemeterSkillTaskExample example);

    public DemeterSkillTask selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") DemeterSkillTask record, @Param("example") DemeterSkillTaskExample example);

    public int updateByExample(@Param("record") DemeterSkillTask record, @Param("example") DemeterSkillTaskExample example);

    public int updateByPrimaryKeySelective(DemeterSkillTask record);

    public int updateByPrimaryKey(DemeterSkillTask record);
}