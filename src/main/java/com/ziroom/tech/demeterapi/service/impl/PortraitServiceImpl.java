package com.ziroom.tech.demeterapi.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.ziroom.tech.demeterapi.common.CodeAnalysisComponent;
import com.ziroom.tech.demeterapi.common.EhrComponent;
import com.ziroom.tech.demeterapi.common.OmegaComponent;
import com.ziroom.tech.demeterapi.common.OperatorContext;
import com.ziroom.tech.demeterapi.common.WorktopComponent;
import com.ziroom.tech.demeterapi.common.enums.*;
import com.ziroom.tech.demeterapi.common.exception.BusinessException;
import com.ziroom.tech.demeterapi.dao.entity.*;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterAssignTaskDao;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterSkillTaskDao;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterTaskUserDao;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.CTOReq;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.EmployeeListReq;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.DailyTaskReq;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.EngineeringMetricReq;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.PortrayalInfoReq;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.EhrDeptResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.EhrJoinTimeResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.EhrUserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.EhrUserResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.halo.AuthResp;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.*;
import com.ziroom.tech.demeterapi.po.dto.resp.task.EmployeeListResp;
import com.ziroom.tech.demeterapi.po.dto.resp.worktop.KVResp;
import com.ziroom.tech.demeterapi.po.dto.resp.worktop.WorktopOverview;
import com.ziroom.tech.demeterapi.service.HaloService;
import com.ziroom.tech.demeterapi.service.PortraitService;
import com.ziroom.tech.demeterapi.service.TreeService;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    private OmegaComponent omegaComponent;
    @Resource
    private TreeService treeService;
    @Resource
    private HaloService haloService;
    @Resource
    private WorktopComponent worktopComponent;

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
        demeterTaskUserExampleCriteria.andTaskTypeEqualTo(TaskType.ASSIGN.getCode());
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

        UserInfo userInfo = UserInfo.builder()
                .email(userDetail.getEmail())
                .hireDays(days)
                .education(userDetail.getHighestEducation())
                .job(userDetail.getJob())
                .position(userDetail.getLevelName())
                .username(userDetail.getUserName())
                .build();

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
            Map<Integer, Long> res = new TreeMap<>();
            skillMap.keySet().forEach(skillId -> {
                long count = skillMap.get(skillId).stream().mapToInt(DemeterSkillTask::getSkillReward).sum();
                res.put(skillId, count);
            });
            List<Map.Entry<Integer, Long>> skillList = res.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toList());
            List<Map.Entry<Integer, Long>> radarGraphs = skillList.stream().limit(6).collect(Collectors.toList());
            List<RadarGraph> radarGraphList = new ArrayList<>(6);
            List<RadarGraph> skills = new ArrayList<>(6);
            for (int i = 0; i < 6; i++) {
                if (radarGraphs.size() - 1 >= i) {
                    Map.Entry<Integer, Long> entry = radarGraphs.get(i);
                    SkillTree skillTree = treeService.selectByPrimaryKey(entry.getKey());
                    RadarGraph graph = new RadarGraph();
                    graph.setText(skillTree.getName());
                    graph.setMax(entry.getValue());
                    radarGraphList.add(graph);
                    skills.add(graph);
                } else {
                    RadarGraph graph = new RadarGraph();
                    graph.setText("待认证技能");
                    graph.setMax(0L);
                    radarGraphList.add(graph);
                }
            }
            resp.setRadarGraphs(radarGraphList);
            userInfo.setSkills(skills.stream().map(RadarGraph::getText).collect(Collectors.joining("|")));
        }

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

    @Override
    public CtoResp getCtoData(CTOReq ctoReq) {
        CtoResp ctoResp = new CtoResp();

        // 部门占比 name:技术保障中心 value: 45
        Set<EhrDeptResp> childOrgList = ehrComponent.getChildOrgs(ctoReq.getDeptId(), "101");
        List<Struct> deptList = new ArrayList<>(16);
        childOrgList.forEach(dept -> {
            Set<EhrUserResp> users = ehrComponent.getUsers(dept.getCode(), 101);
            Struct dep = Struct.builder()
                    .name(dept.getName())
                    .value(users.size())
                    .build();
            deptList.add(dep);
        });
        deptList.sort(Comparator.comparing(Struct::getValue).reversed());
        ctoResp.setDeptInfo(deptList);

        // 职务占比
        Set<EhrUserResp> users = ehrComponent.getUsers(ctoReq.getDeptId(), 101);
        List<String> userCodes = users.stream().map(EhrUserResp::getUserCode).distinct().collect(Collectors.toList());
        Set<EhrUserDetailResp> userRespSet = new HashSet<>(512);

        List<List<String>> partition = Lists.partition(userCodes, 10);
        partition.forEach(codeGroup -> {
            String codeString = String.join(",", codeGroup);
            List<EhrUserDetailResp> ehrUserDetail = ehrComponent.getEhrUserDetail(codeString);
            if (CollectionUtils.isNotEmpty(ehrUserDetail)) {
                userRespSet.addAll(ehrUserDetail);
            }
        });

        List<Struct> jobList = new ArrayList<>(16);
        userRespSet.stream().collect(
                Collectors.groupingBy(x -> Optional.ofNullable(x.getDesc()).orElse(""), Collectors.counting()))
        .forEach((key, value) -> {
            Struct job = Struct.builder()
                    .name(key)
                    .value(value.intValue())
                    .build();
            jobList.add(job);
        });
        jobList.sort(Comparator.comparing(Struct::getValue).reversed());
        ctoResp.setJobInfo(jobList);

        // 职级占比
        List<Struct> levelList = new ArrayList<>(16);
        userRespSet.stream().collect(
                Collectors.groupingBy(x -> Optional.ofNullable(x.getLevelName()).orElse(""), Collectors.counting()))
                .forEach((key, value) -> {
                    Struct level = Struct.builder()
                            .name(key)
                            .value(value.intValue())
                            .build();
                    levelList.add(level);
                });
        levelList.sort(Comparator.comparing(Struct::getValue).reversed());
        ctoResp.setLevelInfo(levelList);

        // 入职年限占比
        List<Struct> hireDateList = new ArrayList<>(16);
        Map<String, Integer> hireDateInfo = new HashMap<>(16);
        hireDateInfo.put("<1年", 0);
        hireDateInfo.put("1-3年", 0);
        hireDateInfo.put("3-5年", 0);
        hireDateInfo.put("5-10年", 0);
        hireDateInfo.put(">10年", 0);
        userCodes.forEach(userCode -> {
            List<EhrJoinTimeResp> jointime = ehrComponent.getJointime(userCode);
            if (CollectionUtils.isNotEmpty(jointime)) {
                EhrJoinTimeResp ehrJoinTimeResp = jointime.get(0);
                LocalDate entryDate = LocalDate.parse(ehrJoinTimeResp.getEntryTime(), DateTimeFormatter.ofPattern("yyyyMMdd"));
                long between = ChronoUnit.YEARS.between(entryDate, LocalDate.now());
                if (between == 0) {
                    Integer value = hireDateInfo.get("<1年");
                    hireDateInfo.put("<1年", ++value);
                } else if (between >= 1 && between < 3) {
                    Integer value = hireDateInfo.get("1-3年");
                    hireDateInfo.put("1-3年", ++value);
                } else if (between >= 3 && between < 5) {
                    Integer value = hireDateInfo.get("3-5年");
                    hireDateInfo.put("3-5年", ++value);
                } else if (between >= 5 && between < 10) {
                    Integer value = hireDateInfo.get("5-10年");
                    hireDateInfo.put("5-10年", ++value);
                } else if (between >= 10) {
                    Integer value = hireDateInfo.get(">10年");
                    hireDateInfo.put(">10年", ++value);
                }
            }
        });
        hireDateInfo.forEach((key, value) -> {
            Struct level = Struct.builder()
                    .name(key)
                    .value(value)
                    .build();
            hireDateList.add(level);
        });
        hireDateList.sort(Comparator.comparing(Struct::getValue).reversed());
        ctoResp.setHireDateInfo(hireDateList);
        return ctoResp;
    }

    @Override
    public CtoDevResp getCtoDevData(CTOReq ctoReq) {
        return codeAnalysisComponent.getDepartmentDe(ctoReq.getDeptId(), ctoReq.getStartDate(), ctoReq.getEndDate());
    }

    @Override
    public Object getCtoProjectData(CTOReq ctoReq) {
        return null;
    }

    @Override
    public CtoOmegaResp getCtoOmegaData(CTOReq ctoReq) throws Exception {
        CtoOmegaResp resp = new CtoOmegaResp();
        JSONArray deployNorm =
                omegaComponent.getDeployNorm(ctoReq.getDeptId(), ctoReq.getStartDate(), ctoReq.getEndDate());
        List<CtoOmegaResp> response = new ArrayList<>(16);
        for (Object o : deployNorm) {
            if (o instanceof LinkedHashMap) {
                response.add(mapToObject((LinkedHashMap)o, CtoOmegaResp.class));
            }
        }
        resp.setCiNum(response.stream().mapToInt(CtoOmegaResp::getCiNum).sum());
        resp.setDeploymentNum(response.stream().mapToInt(CtoOmegaResp::getCiNum).sum());
        resp.setRestartNum(response.stream().mapToInt(CtoOmegaResp::getRestartNum).sum());
        resp.setOnlineNum(response.stream().mapToInt(CtoOmegaResp::getOnlineNum).sum());
        resp.setRollbackNum(response.stream().mapToInt(CtoOmegaResp::getRollbackNum).sum());
        return resp;
    }

    @Override
    public WorktopOverview getWorktopOverview(CTOReq ctoReq) throws Exception {
        WorktopOverview worktopOverview = WorktopOverview.builder().build();
        JSONArray worktopResp =
                worktopComponent.getWorktopOverview(ctoReq.getDeptId(), ctoReq.getStartDate(), ctoReq.getEndDate());
        List<KVResp> respList = new ArrayList<>(16);
        for (Object o : worktopResp) {
            if (o instanceof LinkedHashMap) {
                KVResp resp = mapToObject((LinkedHashMap) o, KVResp.class);
                respList.add(resp);
            }
        }
        Map<String, KVResp> kvRespMap = respList.stream().collect(Collectors.toMap(KVResp::getKey, Function.identity()));
        return WorktopOverview.builder()
                .projectAvg(Optional.ofNullable(kvRespMap.get("projectAvg")).map(KVResp::getValue).orElse(""))
                .projectCount(Optional.ofNullable(kvRespMap.get("projectCount")).map(KVResp::getValue).orElse(""))
                .taskAvg(Optional.ofNullable(kvRespMap.get("taskAvg")).map(KVResp::getValue).orElse(""))
                .taskCount(Optional.ofNullable(kvRespMap.get("taskCount")).map(KVResp::getValue).orElse(""))
                .workTimeCount(Optional.ofNullable(kvRespMap.get("workTimeCount")).map(KVResp::getValue).orElse(""))
                .build();
    }

    public static <T> T mapToObject(Map<Object, Object> map, Class<T> beanClass) throws Exception {
        if (map == null) {
            return null;
        }
        T obj = beanClass.newInstance();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            if (map.containsKey(field.getName())) {
                field.set(obj, map.get(field.getName()));
            }
        }
        return obj;
    }
}

