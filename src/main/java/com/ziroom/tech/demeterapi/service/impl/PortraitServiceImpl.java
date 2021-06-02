package com.ziroom.tech.demeterapi.service.impl;

import com.google.common.collect.Lists;
import com.ziroom.tech.demeterapi.common.CodeAnalysisComponent;
import com.ziroom.tech.demeterapi.common.EhrComponent;
import com.ziroom.tech.demeterapi.common.OperatorContext;
import com.ziroom.tech.demeterapi.common.enums.*;
import com.ziroom.tech.demeterapi.common.exception.BusinessException;
import com.ziroom.tech.demeterapi.dao.entity.*;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterAssignTaskDao;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterSkillTaskDao;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterTaskUserDao;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.EmployeeListReq;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.DailyTaskReq;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.EngineeringMetricReq;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.PortrayalInfoReq;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.EhrJoinTimeResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.EhrUserResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.halo.AuthResp;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.*;
import com.ziroom.tech.demeterapi.po.dto.resp.task.EmployeeListResp;
import com.ziroom.tech.demeterapi.service.HaloService;
import com.ziroom.tech.demeterapi.service.PortraitService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author daijiankun
 */
@Service
public class PortraitServiceImpl implements PortraitService {

    @Resource
    private EhrComponent ehrComponent;
    @Resource
    private DemeterSkillTaskDao demeterSkillTaskDao;
    @Resource
    private DemeterAssignTaskDao demeterAssignTaskDao;
    @Resource
    private DemeterTaskUserDao demeterTaskUserDao;
    @Resource
    private CodeAnalysisComponent codeAnalysisComponent;

    @Resource
    private HaloService haloService;

    @Override
    public List<EmployeeListResp> getEmployeeList(EmployeeListReq employeeListReq) {
        Set<EhrUserResp> users = ehrComponent.getUsers(employeeListReq.getDeptNo(), 101);
        List<String> uidList = users.stream().map(EhrUserResp::getUserCode).collect(Collectors.toList());
        Map<String, EhrUserResp> userMap = users.stream().collect(Collectors.toMap(EhrUserResp::getUserCode, Function.identity()));
        return this.queryEmployeeInfo(uidList, employeeListReq.getStartTime(), employeeListReq.getEndTime());
    }

    @Override
    public DailyTaskResp getDailyTaskInfo(DailyTaskReq dailyTaskReq) {
        DailyTaskResp resp = new DailyTaskResp();
        // 我接收的
        DemeterTaskUserExample demeterTaskUserExample = new DemeterTaskUserExample();
        DemeterTaskUserExample.Criteria demeterTaskUserExampleCriteria = demeterTaskUserExample.createCriteria();
        demeterTaskUserExampleCriteria.andReceiverUidEqualTo(dailyTaskReq.getUid());
        Date startTime = dailyTaskReq.getStartTime();
        Date endTime = dailyTaskReq.getEndTime();
        if (Objects.nonNull(startTime) && Objects.nonNull(endTime)) {
            demeterTaskUserExampleCriteria.andCreateTimeBetween(dailyTaskReq.getStartTime(), dailyTaskReq.getEndTime());
        }
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(demeterTaskUserExample);
        ReceiveMetricsResp receivedResp = new ReceiveMetricsResp();
        receivedResp.setUnclaimed(demeterTaskUsers.stream().filter(d -> d.getTaskStatus().equals(AssignTaskFlowStatus.UNCLAIMED.getCode())).count());
        receivedResp.setRejected(demeterTaskUsers.stream().filter(d -> d.getTaskStatus().equals(AssignTaskFlowStatus.REJECTED.getCode())).count());
        receivedResp.setOngoing(demeterTaskUsers.stream().filter(d -> d.getTaskStatus().equals(AssignTaskFlowStatus.ONGOING.getCode())).count());
        receivedResp.setAcceptance(demeterTaskUsers.stream().filter(d -> d.getTaskStatus().equals(AssignTaskFlowStatus.ACCEPTANCE.getCode())).count());
        receivedResp.setFailed(demeterTaskUsers.stream().filter(d -> d.getTaskStatus().equals(AssignTaskFlowStatus.FAILED.getCode())).count());
        receivedResp.setFinished(demeterTaskUsers.stream().filter(d -> d.getTaskStatus().equals(AssignTaskFlowStatus.FINISHED.getCode())).count());
        receivedResp.setUnfinished(demeterTaskUsers.stream().filter(d -> d.getTaskStatus().equals(AssignTaskFlowStatus.UNFINISHED.getCode())).count());
        receivedResp.setWaitingAccept(demeterTaskUsers.stream().filter(d -> d.getTaskStatus().equals(AssignTaskFlowStatus.WAIT_ACCEPTANCE.getCode())).count());
        resp.setReceived(receivedResp);
        resp.setReceivedCount(demeterTaskUsers.size());

        ReleaseMetricsResp releaseMetricsResp = new ReleaseMetricsResp();
        DemeterAssignTaskExample demeterAssignTaskExample = new DemeterAssignTaskExample();
        DemeterAssignTaskExample.Criteria criteria = demeterAssignTaskExample.createCriteria();
        criteria.andPublisherEqualTo(dailyTaskReq.getUid());
        if (Objects.nonNull(startTime) && Objects.nonNull(endTime)) {
            criteria.andCreateTimeBetween(dailyTaskReq.getStartTime(), dailyTaskReq.getEndTime());
        }
        List<DemeterAssignTask> demeterAssignTasks = demeterAssignTaskDao.selectByExample(demeterAssignTaskExample);
        releaseMetricsResp.setOngoing(demeterAssignTasks.stream().filter(d -> d.getTaskStatus().equals(AssignTaskStatus.ONGOING.getCode())).count());
        releaseMetricsResp.setClosed(demeterAssignTasks.stream().filter(d -> d.getTaskStatus().equals(AssignTaskStatus.CLOSED.getCode())).count());
        releaseMetricsResp.setCompleted(demeterAssignTasks.stream().filter(d -> d.getTaskStatus().equals(AssignTaskStatus.COMPLETED.getCode())).count());
        resp.setReleased(releaseMetricsResp);
        resp.setReleasedCount(demeterAssignTasks.size());
        return resp;
    }

    @Override
    public PortrayalInfoResp getPortrayalInfo(PortrayalInfoReq portrayalInfoReq) {
        CurrentRole currentRole = this.getCurrentRole();
        if (currentRole.equals(CurrentRole.PLAIN)) {
            if (!portrayalInfoReq.getUid().equals(OperatorContext.getOperator())) {
                throw new BusinessException("无权限");
            }
        } else if (currentRole.equals(CurrentRole.DEPT)) {
            UserDetailResp userDetail = ehrComponent.getUserDetail(OperatorContext.getOperator());
            if (Objects.nonNull(userDetail)) {
                Set<EhrUserResp> users = ehrComponent.getUsers(userDetail.getDeptCode(), 101);
                List<String> uidList = users.stream().map(EhrUserResp::getUserCode).collect(Collectors.toList());
                if (!uidList.contains(portrayalInfoReq.getUid())) {
                    throw new BusinessException("无权限");
                }
            } else {
                throw new BusinessException("当前登录用户在ehr查询信息失败。");
            }
        }

        PortrayalInfoResp resp = new PortrayalInfoResp();
        Date startTime = portrayalInfoReq.getStartTime();
        Date endTime = portrayalInfoReq.getEndTime();
        UserDetailResp userDetail = ehrComponent.getUserDetail(portrayalInfoReq.getUid());
        List<EhrJoinTimeResp> jointime = ehrComponent.getJointime(portrayalInfoReq.getUid());
        long days = 0;
        if (CollectionUtils.isNotEmpty(jointime)) {
            EhrJoinTimeResp ehrJoinTimeResp = jointime.get(0);
            LocalDate entryDate = LocalDate.parse(ehrJoinTimeResp.getEntryTime(), DateTimeFormatter.ofPattern("yyyyMMdd"));
            days = LocalDate.now().toEpochDay() - entryDate.toEpochDay();
        }

        // 雷达图数据：技能
        DemeterTaskUserExample taskUserExample = new DemeterTaskUserExample();
        taskUserExample.createCriteria()
                .andTaskTypeEqualTo(TaskType.SKILL.getCode())
                .andReceiverUidEqualTo(portrayalInfoReq.getUid())
                .andTaskStatusEqualTo(SkillTaskFlowStatus.PASS.getCode())
                .andCheckoutTimeBetween(startTime, endTime);
        List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(taskUserExample);
        List<Long> skillPoints = demeterTaskUsers.stream().map(DemeterTaskUser::getTaskId).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(skillPoints)) {
            DemeterSkillTaskExample demeterSkillTaskExample = new DemeterSkillTaskExample();
            demeterSkillTaskExample.createCriteria()
                    .andIdIn(skillPoints);
            List<DemeterSkillTask> demeterSkillTasks = demeterSkillTaskDao.selectByExample(demeterSkillTaskExample);
            Map<Integer, List<DemeterSkillTask>> skillMap = demeterSkillTasks.parallelStream().collect(Collectors.groupingBy(DemeterSkillTask::getSkillId));
            Map<Integer, Long> res = new HashMap<>(16);
            skillMap.keySet().forEach(skillId -> {
                long count = skillMap.get(skillId).stream().map(DemeterSkillTask::getSkillReward).count();
                res.put(skillId, count);
            });
//            res.entrySet().stream()
//                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap())
        }



        UserInfo userInfo = UserInfo.builder()
                .email(userDetail.getEmail())
                .hireDays(days)
                .education(userDetail.getHighestEducation())
                .job(userDetail.getJob())
                .position(userDetail.getLevelName())
                .skills("@daijr，@daijr，@daijr")
                .username(userDetail.getUserName())
                .build();
        resp.setUserInfo(userInfo);
        List<EmployeeListResp> respList = this.queryEmployeeInfo(Lists.newArrayList(portrayalInfoReq.getUid()), startTime, endTime);
        if (CollectionUtils.isNotEmpty(respList)) {
            EmployeeListResp employeeListResp = respList.get(0);
            GrowthInfo growthInfo = GrowthInfo.builder()
                    .growthValue(employeeListResp.getGrowthValue())
                    .assignTaskCount(employeeListResp.getAssignTaskCount())
                    .skillAuthCount(employeeListResp.getSkillCount())
                    .skillGraphCount(employeeListResp.getGraphCount())
                    .skillTaskCount(employeeListResp.getSkillTaskCount())
                    .skillValue(employeeListResp.getSkillValue())
                    .build();
            resp.setGrowthInfo(growthInfo);
        }

        List<Integer> growthValueAcc = new ArrayList<>(16);
        List<Date> growthValueDate = new ArrayList<>(16);

        // 无需验收的任务完成状态即可计入总成长值
        DemeterTaskUserExample assignExample = new DemeterTaskUserExample();
        assignExample.createCriteria()
                .andTaskTypeEqualTo(TaskType.ASSIGN.getCode())
                .andCheckResultEqualTo(CheckoutResult.NO_CHECKOUT.getCode())
                .andReceiverUidEqualTo(portrayalInfoReq.getUid())
                .andTaskStatusEqualTo(AssignTaskFlowStatus.FINISHED.getCode())
                .andCheckoutTimeBetween(startTime, endTime);
        List<DemeterTaskUser> demeterTaskUsers1 = demeterTaskUserDao.selectByExample(assignExample);

        assignExample.clear();

        // 需要验收的任务需验收通过才计入总成长值
        assignExample.createCriteria()
                .andTaskTypeEqualTo(TaskType.ASSIGN.getCode())
                .andCheckResultEqualTo(CheckoutResult.SUCCESS.getCode())
                .andReceiverUidEqualTo(portrayalInfoReq.getUid())
                .andTaskStatusEqualTo(AssignTaskFlowStatus.ACCEPTANCE.getCode())
                .andCheckoutTimeBetween(startTime, endTime);
        List<DemeterTaskUser> demeterTaskUsers2 = demeterTaskUserDao.selectByExample(assignExample);

        List<Long> assignIdList = Stream.concat(demeterTaskUsers1.stream().map(DemeterTaskUser::getTaskId), demeterTaskUsers2.stream().map(DemeterTaskUser::getTaskId)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(assignIdList)) {
            DemeterAssignTaskExample demeterAssignTaskExample = new DemeterAssignTaskExample();
            demeterAssignTaskExample.createCriteria()
                    .andIdIn(assignIdList);
            List<DemeterAssignTask> demeterAssignTasks = demeterAssignTaskDao.selectByExample(demeterAssignTaskExample);
            Map<Long, DemeterAssignTask> assignTaskMap = demeterAssignTasks.stream().collect(Collectors.toMap(DemeterAssignTask::getId, Function.identity()));

            demeterTaskUsers1.forEach(task -> {
                DemeterAssignTask demeterAssignTask = assignTaskMap.get(task.getTaskId());
                growthValueAcc.add(demeterAssignTask.getTaskReward());
                growthValueDate.add(task.getCheckoutTime());
            });

            demeterTaskUsers2.forEach(task -> {
                DemeterAssignTask demeterAssignTask = assignTaskMap.get(task.getTaskId());
                growthValueAcc.add(demeterAssignTask.getTaskReward());
                growthValueDate.add(task.getCheckoutTime());
            });
        }
        resp.getGrowthInfo().setGrowthValueAcc(growthValueAcc);
        resp.getGrowthInfo().setGrowthValueDate(growthValueDate);
        return resp;
    }

    private List<EmployeeListResp> queryEmployeeInfo(List<String> uidList, Date startTime, Date endTime) {
        List<EmployeeListResp> respList = new ArrayList<>(16);
        uidList.forEach(uid -> {
            UserDetailResp userDetail = ehrComponent.getUserDetail(uid);
            EmployeeListResp resp = new EmployeeListResp();
            resp.setJobName(userDetail.getJob());
            resp.setName(userDetail.getUserName());

            // 仅认证通过的技能类任务可计算技能值
            DemeterTaskUserExample skillExample = new DemeterTaskUserExample();
            DemeterTaskUserExample.Criteria criteria = skillExample.createCriteria();
            criteria.andTaskTypeEqualTo(TaskType.SKILL.getCode())
                    .andTaskStatusEqualTo(SkillTaskFlowStatus.PASS.getCode())
                    .andReceiverUidEqualTo(uid);

            if (Objects.nonNull(startTime) && Objects.nonNull(endTime)) {
                criteria.andCheckoutTimeBetween(startTime, endTime);
            }
            List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(skillExample);
            List<Long> skillIdList = demeterTaskUsers.stream().map(DemeterTaskUser::getTaskId).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(skillIdList)) {
                DemeterSkillTaskExample demeterSkillTaskExample = new DemeterSkillTaskExample();
                demeterSkillTaskExample.createCriteria()
                        .andIdIn(skillIdList);
                List<DemeterSkillTask> demeterSkillTasks = demeterSkillTaskDao.selectByExample(demeterSkillTaskExample);
                int skillValue = demeterSkillTasks.stream().mapToInt(DemeterSkillTask::getSkillReward).sum();
                resp.setSkillTaskCount(demeterSkillTasks.size());
                resp.setSkillValue(skillValue);
            }
            // TODO: 2021/5/18 认证技能数量
            resp.setSkillCount(0);

            // 无需验收的任务完成状态即可计入总成长值
            DemeterTaskUserExample assignExample = new DemeterTaskUserExample();
            assignExample.createCriteria()
                    .andTaskTypeEqualTo(TaskType.ASSIGN.getCode())
                    .andCheckResultEqualTo(CheckoutResult.NO_CHECKOUT.getCode())
                    .andReceiverUidEqualTo(uid)
                    .andTaskStatusEqualTo(AssignTaskFlowStatus.FINISHED.getCode());
            List<DemeterTaskUser> demeterTaskUsers1 = demeterTaskUserDao.selectByExample(assignExample);

            assignExample.clear();

            // 需要验收的任务需验收通过才计入总成长值
            assignExample.createCriteria()
                    .andTaskTypeEqualTo(TaskType.ASSIGN.getCode())
                    .andCheckResultEqualTo(CheckoutResult.SUCCESS.getCode())
                    .andReceiverUidEqualTo(uid)
                    .andTaskStatusEqualTo(AssignTaskFlowStatus.ACCEPTANCE.getCode());
            List<DemeterTaskUser> demeterTaskUsers2 = demeterTaskUserDao.selectByExample(assignExample);

            List<Long> assignIdList = Stream.concat(demeterTaskUsers1.stream().map(DemeterTaskUser::getTaskId), demeterTaskUsers2.stream().map(DemeterTaskUser::getTaskId)).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(assignIdList)) {
                DemeterAssignTaskExample demeterAssignTaskExample = new DemeterAssignTaskExample();
                demeterAssignTaskExample.createCriteria()
                        .andIdIn(assignIdList);
                List<DemeterAssignTask> demeterAssignTasks = demeterAssignTaskDao.selectByExample(demeterAssignTaskExample);
                int assignValue = demeterAssignTasks.stream().mapToInt(DemeterAssignTask::getTaskReward).sum();
                resp.setGrowthValue(assignValue);
                resp.setAssignTaskCount(demeterAssignTasks.size());
            }
            respList.add(resp);
        });
        return respList;
    }

    @Override
    public EngineeringMetricResp getEngineeringMetrics(EngineeringMetricReq req) {
        return codeAnalysisComponent.getDevelopmentEquivalent(req.getUid(), req.getStartTime(), req.getEndTime());
    }

    private CurrentRole getCurrentRole() {
        AuthResp permission = haloService.getAuth();
        List<String> roles = permission.getRoles();
        if (roles.contains(CurrentRole.SUPER.getCode())) {
            return CurrentRole.SUPER;
        } else if (roles.contains(CurrentRole.DEPT.getCode())) {
            return CurrentRole.DEPT;
        } else {
            return CurrentRole.PLAIN;
        }
    }
}
