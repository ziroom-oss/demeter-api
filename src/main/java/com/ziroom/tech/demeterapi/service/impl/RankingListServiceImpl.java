package com.ziroom.tech.demeterapi.service.impl;

import com.ziroom.tech.demeterapi.common.EhrComponent;
import com.ziroom.tech.demeterapi.dao.entity.ForTaskName;
import com.ziroom.tech.demeterapi.dao.mapper.*;
import com.ziroom.tech.demeterapi.po.dto.req.ranking.RankingReq;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankingInfo;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankingResp;
import com.ziroom.tech.demeterapi.service.RankingListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class RankingListServiceImpl implements RankingListService {

    @Resource
    private DemeterTaskUserDao demeterTaskUserDao;
    @Resource
    private SkillMapSkillDao skillMapSkillDao;
    @Resource
    private SkillTreeDao skillTreeDao;
    @Resource
    private DemeterSkillTaskDao demeterSkillTaskDao;//根据以上已经获取的taskid
    @Resource
    private EhrComponent ehrComponent;

    //技能图谱相关排行榜
    //技能图谱查询
    //日期查询
    @Override
    public RankingResp[] getAllskillmapIndiactorInfo(RankingReq rankingReq) {

        //根据图谱id获取任务id
        List<Long> skillTaskIds = skillMapSkillDao.getSkillTaskIds(rankingReq.getSearchSkillMap());
        //根据任务id获取任务名称
        List<ForTaskName> names = demeterSkillTaskDao.getTasksName(skillTaskIds);
        Map<Long, String> tasksNames = names.stream().collect(Collectors.toMap(ForTaskName::getId, ForTaskName::getTaskName));

        //1、认证技能点数量排序，将sql查询的resultmap处理成前端需要的数据
        RankingResp skillPoint = new RankingResp();
       List<RankingInfo> skillPointPASSED = demeterTaskUserDao.getSkillPointPASSED(rankingReq, skillTaskIds).stream().map(forRanking -> {
                    return RankingInfo.builder()
                            .name(ehrComponent.getUserDetail(forRanking.getReceiverUid()).getUserName())
                            .num(forRanking.getSumAll().toString())
                            .build();
                }).collect(Collectors.toList());
        skillPoint.setRankingList(skillPointPASSED);
        //设置排名
        for (int i = 0; i < skillPointPASSED.size(); i++) {
            if( skillPointPASSED.get(i).getName().equals(ehrComponent.getUserDetail(rankingReq.getUid()).getUserName())){
                skillPoint.setMyRanking(i+1);
            }
        }
        //2、认证技能数量排序，将sql查询的resultmap处理成前端需要的数据
        RankingResp skill = new RankingResp();
        List<RankingInfo> skillPASSED = demeterTaskUserDao.getSkillNumPASSED(rankingReq, skillTaskIds).stream().map(forRanking -> {
             return RankingInfo.builder()
                     .name(ehrComponent.getUserDetail(forRanking.getReceiverUid()).getUserName())
                    .num(forRanking.getSumAll().toString())
                    .build();
        }).collect(Collectors.toList()); //ForRankingTASK
        skill.setRankingList(skillPASSED);
        //设置排名
        for (int i = 0; i < skillPointPASSED.size(); i++) {
            if( skillPASSED.get(i).getName().equals(ehrComponent.getUserDetail(rankingReq.getUid()).getUserName())){
                skill.setMyRanking(i+1);
            }
        }

        //热门相关无“我的排名”
        //3、热门技能点数量排序，将sql查询的resultmap处理成前端需要的数据
        RankingResp skillPointHot = new RankingResp();
        List<RankingInfo> hotSkillPoint = demeterTaskUserDao.getHotSkillPointName(rankingReq, skillTaskIds).stream().map(forRanking -> {
             return RankingInfo.builder()
                    .name(tasksNames.get(forRanking.getTaskId()))
                    .num(forRanking.getSumAll().toString())
                    .build();
        }).collect(Collectors.toList());
        skillPointHot.setRankingList(hotSkillPoint);
        //4、热门技能数量排序，将sql查询的resultmap处理成前端需要的数据
        RankingResp skillHot = new RankingResp();
        List<RankingInfo> hotSkill = demeterTaskUserDao.getHotSkill(rankingReq, skillTaskIds).stream().map(forRanking -> {
             return RankingInfo.builder()
                     .name(skillTreeDao.selectByPrimaryKey(forRanking.getParentId()).getName())
                    .num(forRanking.getSumAll().toString())
                    .build();
        }).collect(Collectors.toList());
        skillHot.setRankingList(hotSkill);

        return new RankingResp[]{skillPoint, skill, skillPointHot, skillHot};
    }


    private String convertEmail2Adcode(String email) {
        return email.split("@")[0];
    }
}
