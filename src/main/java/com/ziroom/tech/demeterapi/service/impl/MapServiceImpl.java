package com.ziroom.tech.demeterapi.service.impl;

import com.ziroom.tech.demeterapi.open.login.model.OperatorContext;
import com.ziroom.tech.demeterapi.common.enums.SkillTaskFlowStatus;
import com.ziroom.tech.demeterapi.common.enums.TaskType;
import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillTask;
import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillTaskExample;
import com.ziroom.tech.demeterapi.dao.entity.DemeterTaskUser;
import com.ziroom.tech.demeterapi.dao.entity.DemeterTaskUserExample;
import com.ziroom.tech.demeterapi.dao.entity.SkillMap;
import com.ziroom.tech.demeterapi.dao.entity.SkillMapSkillBk;
import com.ziroom.tech.demeterapi.dao.entity.SkillMapSkillBkExample;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterSkillTaskDao;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterTaskUserDao;
import com.ziroom.tech.demeterapi.dao.mapper.SkillMapDao;
import com.ziroom.tech.demeterapi.dao.mapper.SkillMapSkillBkDao;
import com.ziroom.tech.demeterapi.po.dto.req.Map.SkillMapListReq;
import com.ziroom.tech.demeterapi.po.dto.resp.map.SummaryData;
import com.ziroom.tech.demeterapi.po.dto.resp.map.SummaryMapResp;
import com.ziroom.tech.demeterapi.service.MapService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class MapServiceImpl implements MapService {

    @Resource
    private SkillMapDao skillMapDao;

    @Resource
    private DemeterTaskUserDao demeterTaskUserDao;

    @Resource
    private DemeterSkillTaskDao demeterSkillTaskDao;

    @Resource
    private SkillMapSkillBkDao skillMapSkillBkDao;


    @Override
    public Integer countBySkillMap(SkillMapListReq skillMapListReq) {
        return skillMapDao.countBySkillMap(skillMapListReq);
    }
    @Override
    public Long insertSelective(SkillMap skillMap) {
        skillMapDao.insertSelective(skillMap);
        return skillMap.getId();
    }
    @Override
    public Integer deleteByPrimaryKey(Long id) {
        return skillMapDao.deleteByPrimaryKey(id);
    }
    @Override
    public List<SkillMap> selectByConditionSelective(SkillMapListReq skillMapListReq) {
        return skillMapDao.selectByConditionSelective(skillMapListReq);
    }
    @Override
    public Integer updateByPrimaryKeySelective(SkillMap skillMap) {
        return skillMapDao.updateByPrimaryKeySelective(skillMap);
    }
    @Override
    public SkillMap selectByPrimaryKey(Long id) {
        return skillMapDao.selectByPrimaryKey(id);
    }

    @Override
    public SummaryData getSkillGraphData() {
        String operator = OperatorContext.getOperator();
        SummaryData summaryData = new SummaryData();

        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria()
                .andTaskTypeEqualTo(TaskType.SKILL.getCode())
                .andReceiverUidEqualTo(operator)
                .andTaskStatusEqualTo(SkillTaskFlowStatus.PASS.getCode());
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        summaryData.setAuthedSkillPointCount(demeterTaskUsers.size());

        List<Long> taskIds = demeterTaskUsers.stream().map(DemeterTaskUser::getTaskId).collect(Collectors.toList());

        List<DemeterSkillTask> demeterSkillTasks = demeterSkillTaskDao.selectByExample(new DemeterSkillTaskExample());

        LinkedHashMap<Integer, List<DemeterSkillTask>> skillIdMap = demeterSkillTasks.stream()
                .collect(Collectors.groupingBy(DemeterSkillTask::getSkillId, LinkedHashMap::new, Collectors.toList()));

        AtomicInteger skillCount = new AtomicInteger();
        skillIdMap.forEach((k, v) -> {
            List<Long> allIds = v.stream().map(DemeterSkillTask::getId).collect(Collectors.toList());
            if (taskIds.containsAll(allIds)) {
                skillCount.getAndIncrement();
            }
        });
        summaryData.setAuthedSkillCount(skillCount.get());

        List<SkillMapSkillBk> skillMapSkillBks = skillMapSkillBkDao.selectByExample(new SkillMapSkillBkExample());
        Map<Integer, List<SkillMapSkillBk>> skillMapSkills =
                skillMapSkillBks.stream().collect(Collectors.groupingBy(SkillMapSkillBk::getSkillMapId));
        List<SummaryMapResp> summaryMapResps = new ArrayList<>(16);
        skillMapSkills.forEach((k, v) -> {
            List<Long> skillTaskIds = v.stream().map(SkillMapSkillBk::getSkillTaskId).collect(Collectors.toList());
            skillTaskIds.retainAll(taskIds);
            if (skillTaskIds.size() < v.size()) {
                double progress = skillTaskIds.size() * 1.0 / v.size();
                SkillMap skillMap = skillMapDao.selectByPrimaryKey(k.longValue());
                SummaryMapResp summaryMapResp = new SummaryMapResp();
                summaryMapResp.setProgress(progress);
                summaryMapResp.setSkillMap(skillMap);
                summaryMapResps.add(summaryMapResp);
            }
        });
        summaryData.setSkillGraph(summaryMapResps);
       return summaryData;
    }

    @Override
    public List<SkillMap> selectAll() {
        return skillMapDao.selectAll();
    }
}
