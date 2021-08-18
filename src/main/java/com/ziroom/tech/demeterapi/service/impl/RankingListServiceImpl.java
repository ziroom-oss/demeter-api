package com.ziroom.tech.demeterapi.service.impl;

import com.ziroom.tech.demeterapi.common.EhrComponent;
import com.ziroom.tech.demeterapi.dao.entity.*;
import com.ziroom.tech.demeterapi.dao.mapper.*;
import com.ziroom.tech.demeterapi.po.dto.req.ranking.RankingReq;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.DeptRankingResp;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankingInfo;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankingResp;
import com.ziroom.tech.demeterapi.po.qo.UserJoinDept;
import com.ziroom.tech.demeterapi.service.RankingListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;


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

    @Resource
    private DemeterUserInfoDao demeterUserInfoDao;

    //技能图谱相关排行榜
    //技能图谱查询
    //日期查询
    @Override
    public RankingResp[] getAllIndividualSkillmap(RankingReq rankingReq) {
        //根据图谱id获取任务id
        List<Long> skillTaskIds = skillMapSkillDao.getSkillTaskIds(rankingReq.getSearchSkillMap());
        //根据任务id获取任务名称
        List<ForTaskName> names = demeterSkillTaskDao.getTasksName(skillTaskIds);
        Map<Long, String> tasksNames = names.stream().collect(Collectors.toMap(ForTaskName::getId, ForTaskName::getTaskName));

        //1、认证技能点数量排序
        RankingResp skillPoint = new RankingResp();
        List<RankingInfo> skillPointPASSED = demeterTaskUserDao.getSkillPointPASSED(rankingReq, skillTaskIds).stream().map(forRanking -> {
           UserDetailResp userDetail = ehrComponent.getUserDetail(forRanking.getReceiverUid());
                    return RankingInfo.builder()
                            .name(userDetail != null ? userDetail.getUserName() : "")
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
        System.out.println("測試測hi測試測試");
        System.out.println(skillPoint.getMyRanking());
        System.out.println(skillPoint.getRankingList());

        //2、认证技能数量排序
        RankingResp skill = new RankingResp();
        List<RankingInfo> skillPASSED = demeterTaskUserDao.getSkillNumPASSED(rankingReq, skillTaskIds).stream().map(forRanking -> {
            UserDetailResp userDetail = ehrComponent.getUserDetail(forRanking.getReceiverUid());
            return RankingInfo.builder()
                    .name(userDetail != null ? userDetail.getUserName() : "")
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
        //3、热门技能点数量排序
        RankingResp skillPointHot = new RankingResp();
        List<RankingInfo> hotSkillPoint = demeterTaskUserDao.getHotSkillPointName(rankingReq, skillTaskIds).stream().map(forRanking -> {
             return RankingInfo.builder()
                    .name(tasksNames.get(forRanking.getTaskId()))
                    .num(forRanking.getSumAll().toString())
                    .build();
        }).collect(Collectors.toList());
        skillPointHot.setRankingList(hotSkillPoint);
        //4、热门技能数量排序
        RankingResp skillHot = new RankingResp();
        List<RankingInfo> hotSkill = demeterTaskUserDao.getHotSkill(rankingReq, skillTaskIds).stream().map(forRanking -> {
            SkillTree skillTree = skillTreeDao.selectByPrimaryKey(forRanking.getParentId());
            return RankingInfo.builder()
                    .name(skillTree != null ? skillTree.getName() : "")
                    .num(forRanking.getSumAll().toString())
                    .build();
        }).collect(Collectors.toList());
        skillHot.setRankingList(hotSkill);

        return new RankingResp[]{skillPoint, skill, skillPointHot, skillHot};
    }



    @Override
    public List<RankingResp> getAllDeptSkillmap(RankingReq rankingReq){

        List<RankingResp> rankingResps = new ArrayList<>();

        return rankingResps;
    }

    //
    public List<Long> getTaskIds(Integer skillMapId){
        //sql语言已做判断
        List<Long> skillTaskIds = skillMapSkillDao.getSkillTaskIds(skillMapId);
        return skillTaskIds;
    }
    @Override
    public List<RankingInfo> getDeptSkillPoint(RankingReq rankingReq) {

        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria()
                .andTaskIdIn(getTaskIds(rankingReq.getSearchSkillMap()))
                .andCheckResultEqualTo(1)//已认证的
                .andCreateTimeBetween(rankingReq.getStartTime(), rankingReq.getEndTime());//时间条件
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);

        //获取receiverUids，去ehr批量查询出部门中心
        List<String> receiverUids = demeterTaskUsers.stream().map((demeterTaskUser) -> {
            return demeterTaskUser.getReceiverUid();
        }).distinct().collect(Collectors.toList());

        /*
         * String： receiver_Uid
         * Long: 分组之后的个数,如下：
         * usercode   count
         * 2131309    10
         * 2131310    20
         */
        Map<String, Long> groupReceiverUid = demeterTaskUsers.stream().collect(groupingBy(DemeterTaskUser::getReceiverUid, counting()));

        //查询员工和部门信息   将receiverUids和部门名称对应
        DemeterUserInfoExample demeterUserInfoExample = new DemeterUserInfoExample();
        demeterUserInfoExample.createCriteria()
                .andUserCodeIn(receiverUids);

        //	肖江		104615	智能平台研发部
        List<DemeterUserInfo> demeterUserInfos = demeterUserInfoDao.selectByExample(demeterUserInfoExample);
        //用户名       用户系统码    部门名      部门编码      总数
        //username    usercode    deptname  deptcode      sumall
        List<UserJoinDept> userJoinDepts = demeterUserInfos.stream().map((demeterUserInfo -> {
            UserJoinDept userJoinDept = new UserJoinDept();
            String userCode = demeterUserInfo.getUserCode();
            userJoinDept.setReceiverUid(userCode);
            userJoinDept.setEmpName(demeterUserInfo.getName());
            userJoinDept.setDeptCode(demeterUserInfo.getDeptCode());
            userJoinDept.setDeptName(demeterUserInfo.getDeptName());
            //每个人认领地技能数量
            if (groupReceiverUid.containsKey(demeterUserInfo.getUserCode())) {
                userJoinDept.setSumAll(groupReceiverUid.get(userCode));
            }
            return userJoinDept;
        })).collect(Collectors.toList());
        //部门关联个人认领技能数量
        /*
         * deptname    skillpointsum
         */
        List<RankingInfo> deptRankingResp = userJoinDepts.stream()
                .collect(groupingBy(UserJoinDept::getDeptName, summingLong(UserJoinDept::getSumAll)))
                //按数量排序
                .entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue)).map(e -> {
                    return new RankingInfo(e.getKey(), e.getValue().toString());
                }).collect(toList());
        // Map<String, List<Long>> deptJoinSkillPointNum = userJoinDepts.stream().collect(groupingBy(UserJoinDept::getDeptName, mapping(UserJoinDept::getSumall, toList())));
        return deptRankingResp;
    }

    @Override
    public List<RankingInfo> getDeptSkill(RankingReq rankingReq) {

        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria()
                .andTaskIdIn(getTaskIds(rankingReq.getSearchSkillMap()))
                .andCheckResultEqualTo(1)//已认证的
                .andCreateTimeBetween(rankingReq.getStartTime(), rankingReq.getEndTime());//时间条件
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);

        DemeterSkillTaskExample demeterSkillTaskExample = new DemeterSkillTaskExample();
        List<DemeterSkillTask> demeterSkillTasks = demeterSkillTaskDao.selectByExample(demeterSkillTaskExample);
        //Map<Integer, List<Long>> skillMapSkills = skillMapSkillBks.stream().collect(groupingBy(SkillMapSkillBk::getSkillMapId, mapping(SkillMapSkillBk::getSkillTaskId, toList())));

        //main
        Map<Integer, Long> parentIds = demeterTaskUsers.stream().collect(groupingBy(DemeterTaskUser::getParentId, counting()));
        //refrence
        Map<Integer, Long> skillIds = demeterSkillTasks.stream().collect(groupingBy(DemeterSkillTask::getSkillId, counting()));
        //

        //匹配符合条件的技能Id
        List<Integer> parents = new ArrayList<>();
        for (Map.Entry<Integer, Long> entryParent : parentIds.entrySet()) {
            for (Map.Entry<Integer, Long> entrySkill : skillIds.entrySet()) {
                if (entrySkill.getKey().equals(entryParent.getKey())) {
                    //如果两个数量一样
                    if(entryParent.getValue().intValue() == entrySkill.getValue().intValue()){
                        parents.add(entryParent.getKey());
                        break;
                    }
                }
            }
        }

        //已认证技能为条件
        DemeterTaskUserExample demeterTaskUserExample2 = new DemeterTaskUserExample();
        demeterTaskUserExample2.createCriteria()
                .andTaskIdIn(getTaskIds(rankingReq.getSearchSkillMap()))
                .andCheckResultEqualTo(1)//已认证的
                .andParentIdIn(parents)
                .andCreateTimeBetween(rankingReq.getStartTime(), rankingReq.getEndTime());//时间条件
        List<DemeterTaskUser> demeterTaskUsers2 = demeterTaskUserDao.selectByExample(demeterTaskUserExample2);

        //获取receiverUids，去ehr批量查询出部门中心
        List<String> receiverUids = demeterTaskUsers2.stream().map((demeterTaskUser2) -> {
            return demeterTaskUser2.getReceiverUid();
        }).distinct().collect(Collectors.toList());

        /*
         * String： receiver_Uid
         * Long: 分组之后的个数,如下：
         * usercode   count
         * 2131309    10
         * 2131310    20
         */
        Map<String, Long> groupReceiverUid = demeterTaskUsers2.stream().collect(groupingBy(DemeterTaskUser::getReceiverUid, counting()));

        //查询员工和部门信息   将receiverUid和部门名称对应
        DemeterUserInfoExample demeterUserInfoExample = new DemeterUserInfoExample();
        demeterUserInfoExample.createCriteria()
                .andUserCodeIn(receiverUids);

        //	肖江		104615	智能平台研发部
        List<DemeterUserInfo> demeterUserInfos = demeterUserInfoDao.selectByExample(demeterUserInfoExample);
        //用户名       用户系统码    部门名      部门编码      总数
        //username    usercode    deptname  deptcode      sumall

        List<UserJoinDept> userJoinDepts = demeterUserInfos.stream().map((demeterUserInfo -> {
            UserJoinDept userJoinDept = new UserJoinDept();
            String userCode = demeterUserInfo.getUserCode();
            userJoinDept.setReceiverUid(userCode);
            userJoinDept.setEmpName(demeterUserInfo.getName());
            userJoinDept.setDeptCode(demeterUserInfo.getDeptCode());
            userJoinDept.setDeptName(demeterUserInfo.getDeptName());
            //每个人认领地技能数量
            if (groupReceiverUid.containsKey(demeterUserInfo.getUserCode())) {
                userJoinDept.setSumAll(groupReceiverUid.get(userCode));
            }
            return userJoinDept;
        })).collect(Collectors.toList());
        //部门关联个人认领技能数量
        /*
         * deptname    skillpointsum
         */
        List<RankingInfo> deptRankingResp = userJoinDepts.stream()
                .collect(groupingBy(UserJoinDept::getDeptName, summingLong(UserJoinDept::getSumAll)))
                //按数量排序
                .entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue)).map(e -> {
                    return new RankingInfo(e.getKey(), e.getValue().toString());
                }).collect(toList());

        return deptRankingResp;
    }


    private String convertEmail2Adcode(String email) {
        return email.split("@")[0];
    }
}
