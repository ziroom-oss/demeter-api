package com.ziroom.tech.demeterapi.service.impl;

import com.ziroom.tech.demeterapi.common.EhrComponent;
import com.ziroom.tech.demeterapi.common.enums.AssignTaskFlowStatus;
import com.ziroom.tech.demeterapi.common.enums.CheckoutResult;
import com.ziroom.tech.demeterapi.common.enums.SkillTaskFlowStatus;
import com.ziroom.tech.demeterapi.common.enums.TaskType;
import com.ziroom.tech.demeterapi.dao.entity.*;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterAssignTaskDao;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterSkillTaskDao;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterTaskUserDao;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.EmployeeListReq;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.EhrUserResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.task.EmployeeListResp;
import com.ziroom.tech.demeterapi.service.PortrayalService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author daijiankun
 */
@Service
public class PortrayalServiceImpl implements PortrayalService {

    @Resource
    private EhrComponent ehrComponent;

    @Resource
    private DemeterSkillTaskDao demeterSkillTaskDao;
    @Resource
    private DemeterAssignTaskDao demeterAssignTaskDao;
    @Resource
    private DemeterTaskUserDao demeterTaskUserDao;

    @Override
    public List<EmployeeListResp> getEmployeeList(EmployeeListReq employeeListReq) {
        // TODO: 2021/5/18 test
        List<EmployeeListResp> respList = new ArrayList<>(16);
        Set<EhrUserResp> users = ehrComponent.getUsers(employeeListReq.getDeptNo(), 101);
        List<String> uidList = users.stream().map(EhrUserResp::getUserCode).collect(Collectors.toList());
        Map<String, EhrUserResp> userMap = users.stream().collect(Collectors.toMap(EhrUserResp::getUserCode, Function.identity()));

        uidList.forEach(uid -> {
            UserDetailResp userDetail = ehrComponent.getUserDetail(uid);
            EmployeeListResp resp = new EmployeeListResp();
            resp.setName(userMap.get(uid).getName());
            resp.setJobName(userDetail.getJob());

            // 仅认证通过的技能类任务可计算技能值
            DemeterTaskUserExample skillExample = new DemeterTaskUserExample();
            skillExample.createCriteria()
                    .andTaskTypeEqualTo(TaskType.SKILL.getCode())
                    .andTaskStatusEqualTo(SkillTaskFlowStatus.PASS.getCode())
                    .andReceiverUidEqualTo(uid)
                    .andCheckoutTimeBetween(employeeListReq.getStartTime(), employeeListReq.getEndTime());
            List<DemeterTaskUser> demeterTaskUsers = demeterTaskUserDao.selectByExample(skillExample);
            List<Long> skillIdList = demeterTaskUsers.stream().map(DemeterTaskUser::getTaskId).collect(Collectors.toList());

            DemeterSkillTaskExample demeterSkillTaskExample = new DemeterSkillTaskExample();
            demeterSkillTaskExample.createCriteria()
                    .andIdIn(skillIdList);
            List<DemeterSkillTask> demeterSkillTasks = demeterSkillTaskDao.selectByExample(demeterSkillTaskExample);
            int skillValue = demeterSkillTasks.stream().mapToInt(DemeterSkillTask::getSkillReward).sum();
            resp.setSkillTaskCount(demeterSkillTasks.size());
            resp.setSkillValue(skillValue);
            // TODO: 2021/5/18 认证技能数量
            resp.setSkillCount(0);


            // 无需验收的任务完成状态即可计入总成长值
            DemeterTaskUserExample assignExample1 = new DemeterTaskUserExample();
            assignExample1.createCriteria()
                    .andTaskTypeEqualTo(TaskType.ASSIGN.getCode())
                    .andCheckResultEqualTo(CheckoutResult.NO_CHECKOUT.getCode())
                    .andTaskStatusEqualTo(AssignTaskFlowStatus.FINISHED.getCode());
            List<DemeterTaskUser> demeterTaskUsers1 = demeterTaskUserDao.selectByExample(assignExample1);

            // 需要验收的任务需验收通过才计入总成长值
            DemeterTaskUserExample assignExample2 = new DemeterTaskUserExample();
            assignExample2.createCriteria()
                    .andTaskTypeEqualTo(TaskType.ASSIGN.getCode())
                    .andCheckResultEqualTo(CheckoutResult.SUCCESS.getCode())
                    .andTaskStatusEqualTo(AssignTaskFlowStatus.ACCEPTANCE.getCode());
            List<DemeterTaskUser> demeterTaskUsers2 = demeterTaskUserDao.selectByExample(assignExample2);

            List<Long> assignIdList = Stream.concat(demeterTaskUsers1.stream().map(DemeterTaskUser::getTaskId), demeterTaskUsers2.stream().map(DemeterTaskUser::getTaskId)).collect(Collectors.toList());

            DemeterAssignTaskExample demeterAssignTaskExample = new DemeterAssignTaskExample();
            demeterAssignTaskExample.createCriteria()
                    .andIdIn(assignIdList);
            List<DemeterAssignTask> demeterAssignTasks = demeterAssignTaskDao.selectByExample(demeterAssignTaskExample);
            int assignValue = demeterAssignTasks.stream().mapToInt(DemeterAssignTask::getTaskReward).sum();
            resp.setGrowthValue(assignValue);
            resp.setAssignTaskCount(demeterAssignTasks.size());

            respList.add(resp);
        });
        return respList;
    }
}
