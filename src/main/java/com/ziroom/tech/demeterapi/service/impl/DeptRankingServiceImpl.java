package com.ziroom.tech.demeterapi.service.impl;

import com.ziroom.tech.demeterapi.common.EhrComponent;
import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillTask;
import com.ziroom.tech.demeterapi.dao.entity.DemeterTaskUser;
import com.ziroom.tech.demeterapi.dao.entity.DemeterTaskUserExample;
import com.ziroom.tech.demeterapi.dao.entity.SkillMapSkill;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterTaskUserDao;
import com.ziroom.tech.demeterapi.dao.mapper.SkillMapSkillDao;
import com.ziroom.tech.demeterapi.po.dto.req.ranking.RankingReq;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.DeptRankingResp;
import com.ziroom.tech.demeterapi.service.DeptRankingService;
import com.ziroom.tech.demeterapi.utils.StringUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class DeptRankingServiceImpl implements DeptRankingService {

    @Resource
    private DemeterTaskUserDao demeterTaskUserDao;
    @Resource
    private SkillMapSkillDao skillMapSkillDao;
    @Resource
    private DemeterTaskUserDao demeterSkillTaskDao;
    @Resource
    private EhrComponent ehrComponent;

    //
    public List<Long> getTaskIds(Integer skillMapId){
        //查询图谱对应的taskid
        List<Long> skillTaskIds = skillMapSkillDao.selectByMapId(skillMapId).stream().map((skillMapSkill) -> {
            return skillMapSkill.getSkillTaskId();
        }).collect(Collectors.toList());
        return skillTaskIds;
    }

    @Override
    public List<DeptRankingResp> getDeptSkillPoint(RankingReq rankingReq) {

        //如果不为空,查询 task_id, receiver_Uid、check_result=1   demeter task user 集合
        if(StringUtil.isEmpty(rankingReq.getSearchSkillMap())){
            DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
            demeterTaskUserExample.createCriteria()
                                    .andTaskIdIn(getTaskIds(rankingReq.getSearchSkillMap()))
                                    .andCheckResultEqualTo(1)//已认证的
                                    .andCreateTimeBetween(rankingReq.getStartTime(), rankingReq.getEndTime());//时间条件
            List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);

            //获取去重后的receiverUids，去ehr批量查询出部门中心
            List<String> receiverUids = demeterTaskUsers.stream().map((demeterTaskUser) -> {
                return demeterTaskUser.getReceiverUid();
            }).collect(Collectors.toList());
           // ehrComponent.getEmpList()


            /**
             * String： receiver_Uid
             * Long: 分组之后的个数
             */
            Map<String, Long> groupReceiverUid = demeterTaskUsers.stream().collect(groupingBy(DemeterTaskUser::getReceiverUid, counting()));

        }

        return null;
    }

    @Override
    public List<DeptRankingResp> getDeptSkill(RankingReq rankingReq) {
        return null;
    }
}
