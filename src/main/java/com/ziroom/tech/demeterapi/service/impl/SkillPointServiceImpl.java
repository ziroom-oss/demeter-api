package com.ziroom.tech.demeterapi.service.impl;

import com.google.common.collect.Lists;
import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillTask;
import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillTaskExample;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterSkillTaskDao;
import com.ziroom.tech.demeterapi.service.SkillPointService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author daijiankun
 */
@Service
@Slf4j
public class SkillPointServiceImpl implements SkillPointService {

    @Resource
    private DemeterSkillTaskDao demeterSkillTaskDao;

    @Override
    public Object querySkillPointFromTreeId(List<Long> skillTreeId) {

        DemeterSkillTaskExample demeterSkillTaskExample = new DemeterSkillTaskExample();
        if (CollectionUtils.isNotEmpty(skillTreeId)) {
            demeterSkillTaskExample.createCriteria()
                    .andSkillIdIn(skillTreeId);
            List<DemeterSkillTask> demeterSkillTasks = demeterSkillTaskDao.selectByExample(demeterSkillTaskExample);
            Map<Long, List<DemeterSkillTask>> treeMap = demeterSkillTasks.stream()
                    .collect(Collectors.groupingBy(DemeterSkillTask::getSkillId));
            return treeMap;
        }
        return Lists.newArrayList();
    }
}
