package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.*;

import java.util.List;

import com.ziroom.tech.demeterapi.po.dto.req.ranking.RankingReq;
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
@Repository
public interface DemeterTaskUserDao {

    public List<ForRankingPASSED> getSkillPointPASSED(@Param("rankingReq") RankingReq rankingReq, @Param("taskIds") List<Long> taskIds); //已认证技能点
    public List<ForRankingTASK> getHotSkillPointName(@Param("rankingReq")RankingReq rankingReq, @Param("taskIds") List<Long> taskIds); //获取热门技能点名称
    public List<ForRankingPASSED> getSkillNumPASSED(@Param("rankingReq")RankingReq rankingReq, @Param("taskIds") List<Long> taskIds); // 认证技能数量
    public List<ForRankingPARENT> getHotSkill(@Param("rankingReq")RankingReq rankingReq, @Param("taskIds") List<Long> taskIds); //热门技能

    public int countByExample(DemeterTaskUserExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(DemeterTaskUser record);

    public int insertSelective(DemeterTaskUser record);

    public List<DemeterTaskUser> selectByExample(DemeterTaskUserExample example);

    public DemeterTaskUser selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") DemeterTaskUser record, @Param("example") DemeterTaskUserExample example);

    public int updateByExample(@Param("record") DemeterTaskUser record, @Param("example") DemeterTaskUserExample example);

    public int updateByPrimaryKeySelective(DemeterTaskUser record);

    public int updateByPrimaryKey(DemeterTaskUser record);
}