package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillLearnPath;
import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillLearnPathExample;
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
public interface DemeterSkillLearnPathDao {

    public int countByExample(DemeterSkillLearnPathExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(DemeterSkillLearnPath record);

    public int insertSelective(DemeterSkillLearnPath record);

    public List<DemeterSkillLearnPath> selectByExample(DemeterSkillLearnPathExample example);

    public DemeterSkillLearnPath selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") DemeterSkillLearnPath record, @Param("example") DemeterSkillLearnPathExample example);

    public int updateByExample(@Param("record") DemeterSkillLearnPath record, @Param("example") DemeterSkillLearnPathExample example);

    public int updateByPrimaryKeySelective(DemeterSkillLearnPath record);

    public int updateByPrimaryKey(DemeterSkillLearnPath record);
}