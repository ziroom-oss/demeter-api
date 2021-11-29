package com.ziroom.tech.demeterapi.open.portrait.person.service.impl;

import com.google.common.collect.Lists;
import com.ziroom.tech.demeterapi.common.OperatorContext;
import com.ziroom.tech.demeterapi.common.enums.SkillTaskFlowStatus;
import com.ziroom.tech.demeterapi.common.enums.TaskType;
import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillTask;
import com.ziroom.tech.demeterapi.dao.entity.DemeterTaskUser;
import com.ziroom.tech.demeterapi.dao.entity.DemeterTaskUserExample;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterSkillTaskDao;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterTaskUserDao;
import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.portrait.person.dto.PortraitDevlopReportDto;
import com.ziroom.tech.demeterapi.open.portrait.person.dto.PortraitPersonGrowingupDto;
import com.ziroom.tech.demeterapi.open.portrait.person.dto.PortraitPersonProjectDto;
import com.ziroom.tech.demeterapi.open.portrait.person.dto.PortraitPersonSkillDto;
import com.ziroom.tech.demeterapi.open.portrait.person.param.PortraitPersonReqParam;
import com.ziroom.tech.demeterapi.open.portrait.person.service.PortraitPersonService;
import com.ziroom.tech.demeterapi.open.common.utils.ModelResultUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 此类作为测试使用
 * @author xuzeyu
 */
@Service("test")
public class TestPortraitPersonServiceImpl implements PortraitPersonService {

    @Autowired
    private DemeterSkillTaskDao demeterSkillTaskDao;

    @Autowired
    private DemeterTaskUserDao demeterTaskUserDao;

    /**
     * 获取个人成长信息展现
     */
    public ModelResult<List<PortraitPersonGrowingupDto>> getUserGrowingupInfo(String uid) {
        List<PortraitPersonGrowingupDto> portraitPersonGrowingupDtos = Lists.newArrayList();

        /**
         * 展示样式
         * type=1 单个指标样式展示 指标数据样式居中
         * type=2 两个指标样式展示 分列到两端
         * type=3 3-4个指标样式展示
         */

        //type=1
        PortraitPersonGrowingupDto portraitPersonGrowingupDto = new PortraitPersonGrowingupDto();
        portraitPersonGrowingupDto.setTitle("OKR");
        portraitPersonGrowingupDto.setType(1);
        List<PortraitPersonGrowingupDto.Point> points = Lists.newArrayList();
        PortraitPersonGrowingupDto.Point point = new PortraitPersonGrowingupDto.Point();
        point.setCoreName("OKR完成率");
        point.setCoreData("88%");
        points.add(point);
        portraitPersonGrowingupDto.setPoints(points);

        //type=2
        PortraitPersonGrowingupDto portraitPersonGrowingupDto2 = new PortraitPersonGrowingupDto();
        portraitPersonGrowingupDto2.setTitle("技术分享");
        portraitPersonGrowingupDto2.setType(2);
        List<PortraitPersonGrowingupDto.Point> points2 = Lists.newArrayList();
        PortraitPersonGrowingupDto.Point point2 = new PortraitPersonGrowingupDto.Point();
        point2.setCoreName("发表文章数");
        point2.setCoreData("5");
        PortraitPersonGrowingupDto.Point point3 = new PortraitPersonGrowingupDto.Point();
        point3.setCoreName("获奖数");
        point3.setCoreData("2");
        points2.add(point2);
        points2.add(point3);
        portraitPersonGrowingupDto2.setPoints(points2);

        //type=3
        PortraitPersonGrowingupDto portraitPersonGrowingupDto3 = new PortraitPersonGrowingupDto();
        portraitPersonGrowingupDto3.setTitle("自如大学");
        portraitPersonGrowingupDto3.setType(3);
        List<PortraitPersonGrowingupDto.Point> points3= Lists.newArrayList();
        PortraitPersonGrowingupDto.Point point4 = new PortraitPersonGrowingupDto.Point();
        point4.setCoreName("学习次数");
        point4.setCoreData("5");
        PortraitPersonGrowingupDto.Point point5 = new PortraitPersonGrowingupDto.Point();
        point5.setCoreName("学习时长");
        point5.setCoreData("200min");
        PortraitPersonGrowingupDto.Point point6 = new PortraitPersonGrowingupDto.Point();
        point6.setCoreName("分享次数");
        point6.setCoreData("2");
        points3.add(point4);
        points3.add(point5);
        points3.add(point6);
        portraitPersonGrowingupDto3.setPoints(points3);

        portraitPersonGrowingupDtos.add(portraitPersonGrowingupDto);
        portraitPersonGrowingupDtos.add(portraitPersonGrowingupDto2);
        portraitPersonGrowingupDtos.add(portraitPersonGrowingupDto3);
        return ModelResultUtil.success(portraitPersonGrowingupDtos);
    }

    /**
     * 获取个人开发指标表现
     * @return
     */
    public ModelResult<PortraitDevlopReportDto> getPortraitPersonDevModel(PortraitPersonReqParam personReqParam){
        PortraitDevlopReportDto portraitDevlopReportDto = new PortraitDevlopReportDto();
        portraitDevlopReportDto.setDevEquivalent(5005L);
        portraitDevlopReportDto.setInsertions(5100L);
        portraitDevlopReportDto.setDeletions(100L);
        portraitDevlopReportDto.setCommitCount(20);
        portraitDevlopReportDto.setProjectNum(3);
        portraitDevlopReportDto.setDemandNum(13);
        portraitDevlopReportDto.setBugNum(2);
        portraitDevlopReportDto.setDevValue("16");
        portraitDevlopReportDto.setProjectAveDevPeriod("35");
        portraitDevlopReportDto.setFunctionAveDevPeriod("4");
        portraitDevlopReportDto.setBugAveFixTime("1");
        portraitDevlopReportDto.setPublishNum(6);
        portraitDevlopReportDto.setCompileNum(6);
        portraitDevlopReportDto.setOnlineNum(2);
        portraitDevlopReportDto.setRestartNum(1);
        portraitDevlopReportDto.setRollbackNum(1);
        portraitDevlopReportDto.setDocCoverage("80");
        portraitDevlopReportDto.setStaticTestCoverage("88");
        portraitDevlopReportDto.setFunImpact("60");
        portraitDevlopReportDto.setBugProbability("10");
        portraitDevlopReportDto.setWorkHours("18");
        portraitDevlopReportDto.setDevlopHours("18");
        portraitDevlopReportDto.setWorkSaturability("100");
        portraitDevlopReportDto.setVacationDays("1");
        return ModelResultUtil.success(portraitDevlopReportDto);
    }

    /**
     * 指标数据团队平均值展现
     */
    public ModelResult<PortraitDevlopReportDto> getTeamDevlopPortraitData(PortraitPersonReqParam personReqParam) {
        PortraitDevlopReportDto portraitDevlopReportDto = new PortraitDevlopReportDto();
        portraitDevlopReportDto.setDevEquivalent(6005L);
        portraitDevlopReportDto.setInsertions(2000L);
        portraitDevlopReportDto.setDeletions(150L);
        portraitDevlopReportDto.setCommitCount(120);
        portraitDevlopReportDto.setProjectNum(1);
        portraitDevlopReportDto.setDemandNum(10);
        portraitDevlopReportDto.setBugNum(10);
        portraitDevlopReportDto.setDevValue("15");
        portraitDevlopReportDto.setProjectAveDevPeriod("11");
        portraitDevlopReportDto.setFunctionAveDevPeriod("23");
        portraitDevlopReportDto.setBugAveFixTime("12");
        portraitDevlopReportDto.setPublishNum(10);
        portraitDevlopReportDto.setCompileNum(10);
        portraitDevlopReportDto.setOnlineNum(10);
        portraitDevlopReportDto.setRestartNum(10);
        portraitDevlopReportDto.setRollbackNum(10);
        portraitDevlopReportDto.setDocCoverage("90");
        portraitDevlopReportDto.setStaticTestCoverage("90");
        portraitDevlopReportDto.setFunImpact("50");
        portraitDevlopReportDto.setBugProbability("10");
        portraitDevlopReportDto.setWorkHours("13");
        portraitDevlopReportDto.setDevlopHours("13");
        portraitDevlopReportDto.setWorkSaturability("100");
        portraitDevlopReportDto.setVacationDays("1");
        return ModelResultUtil.success(portraitDevlopReportDto);
    }

    /**
     * 个人项目指标数据展现
     */
    public ModelResult<List<PortraitPersonProjectDto>> getProjectPortraitData(PortraitPersonReqParam personReqParam){
        List<PortraitPersonProjectDto> list = Lists.newArrayList();
        PortraitPersonProjectDto portraitPersonProjectDto1 = new PortraitPersonProjectDto();
        portraitPersonProjectDto1.setProjectName("code-analysis");
        portraitPersonProjectDto1.setDevEquivalent(9000L);

        PortraitPersonProjectDto portraitPersonProjectDto2 = new PortraitPersonProjectDto();
        portraitPersonProjectDto2.setProjectName("growingup");
        portraitPersonProjectDto2.setDevEquivalent(5000L);

        PortraitPersonProjectDto portraitPersonProjectDto3 = new PortraitPersonProjectDto();
        portraitPersonProjectDto3.setProjectName("demeter");
        portraitPersonProjectDto3.setDevEquivalent(6000L);

        PortraitPersonProjectDto portraitPersonProjectDto4 = new PortraitPersonProjectDto();
        portraitPersonProjectDto4.setProjectName("suzaku");
        portraitPersonProjectDto4.setDevEquivalent(3000L);

        list.add(portraitPersonProjectDto1);
        list.add(portraitPersonProjectDto2);
        list.add(portraitPersonProjectDto3);
        list.add(portraitPersonProjectDto4);
        return ModelResultUtil.success(list);
    }

    @Override
    public ModelResult<PortraitPersonSkillDto> getPortraitPersonSkillInfo(String uid) {
        PortraitPersonSkillDto portraitPersonSkillDto = new PortraitPersonSkillDto();
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        demeterTaskUserExample.createCriteria()
                .andTaskTypeEqualTo(1)
                .andReceiverUidEqualTo(uid)
                .andTaskStatusEqualTo(SkillTaskFlowStatus.PASS.getCode());
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        if(CollectionUtils.isNotEmpty(demeterTaskUsers)){
            portraitPersonSkillDto.setSkillPointNum(demeterTaskUsers.size());
            List<Long> taskList = demeterTaskUsers.stream().map(DemeterTaskUser::getTaskId).collect(Collectors.toList());
            List<DemeterSkillTask> demeterSkillTasks = demeterSkillTaskDao.selectByTaskIds(taskList);
            Map<Integer, List<DemeterSkillTask>> skillRelTaskMap = demeterSkillTasks.stream().collect(Collectors.groupingBy(DemeterSkillTask::getSkillId, Collectors.toList()));
            if(Objects.nonNull(skillRelTaskMap)){
                portraitPersonSkillDto.setSkillNum(skillRelTaskMap.size());
            }
        }
        return ModelResultUtil.success(portraitPersonSkillDto);
    }
}
