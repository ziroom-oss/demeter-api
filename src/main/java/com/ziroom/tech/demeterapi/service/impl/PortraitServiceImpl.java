package com.ziroom.tech.demeterapi.service.impl;

import com.ziroom.tech.demeterapi.common.enums.*;
import com.ziroom.tech.demeterapi.dao.entity.*;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterAssignTaskDao;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterTaskUserDao;
import com.ziroom.tech.demeterapi.po.dto.req.portrayal.DailyTaskReq;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.*;
import com.ziroom.tech.demeterapi.service.PortraitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author daijiankun
 */
@Service
@Slf4j
public class PortraitServiceImpl implements PortraitService {

    @Resource
    private DemeterAssignTaskDao demeterAssignTaskDao;
    @Resource
    private DemeterTaskUserDao demeterTaskUserDao;

    @Transactional
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


}

