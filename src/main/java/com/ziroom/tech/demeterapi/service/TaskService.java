package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.task.*;
import com.ziroom.tech.demeterapi.po.dto.resp.task.AssignDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.task.ReceiverListResp;
import com.ziroom.tech.demeterapi.po.dto.resp.task.ReleaseQueryResp;

import java.util.List;

/**
 * @author daijiankun
 */
public interface TaskService {

    /**
     * 创建指派类任务
     * @param assignTaskReq 指派类任务请求体
     * @return Resp
     */
    Resp<Object> createAssignTask(AssignTaskReq assignTaskReq);

    /**
     * 创建技能类任务
     * @param skillTaskReq 技能类任务请求体
     * @return Resp
     */
    Resp<Object> createSkillTask(SkillTaskReq skillTaskReq);

    /**
     * 编辑指派类任务
     * @param assignTaskReq 指派类任务请求体
     * @return Resp
     */
    Resp<Object> updateAssignTask(AssignTaskReq assignTaskReq);

    /**
     * 编辑技能类任务
     * @param skillTaskReq  技能类任务请求体
     * @return Resp
     */
    Resp<Object> updateSkillTask(SkillTaskReq skillTaskReq);

    /**
     * 任务列表-我发布的
     * @param taskListQueryReq 任务列表查询请求体
     * @return Resp<List<ReleaseQueryResp>>
     */
    Resp<List<ReleaseQueryResp>> getReleaseList(TaskListQueryReq taskListQueryReq);

    /**
     * 任务列表-我接收的
     * @param taskListQueryReq 任务列表查询请求体
     * @return Resp
     */
    Resp<Object> getExecuteList(TaskListQueryReq taskListQueryReq);

    /**
     * 任务详情-指派类任务
     * @param id 任务id
     * @return Resp<AssignDetailResp>
     */
    @Deprecated
    Resp<AssignDetailResp> getAssignDetail(Long id);

    /**
     * 任务详情-技能类任务
     * @param id 任务id
     * @return Resp
     */
    Resp<Object> getSkillDetail(Long id);

    /**
     * 删除任务
     * @param id 任务id
     * @param taskType 任务类型
     * @return Resp
     */
    Resp<Object> delete(Long id, Integer taskType);

    /**
     * 认领任务
     * @param id 任务id
     * @param type 任务类型
     * @return Resp
     */
    Resp<Object> acceptTask(Long id, Integer type);

    /**
     * 验收/认证 任务
     * @param checkTaskReq 验收任务请求体
     * @return Resp
     */
    Resp<Object> checkTask(CheckTaskReq checkTaskReq);

    /**
     * 拒绝指派类任务
     * @param rejectTaskReq 指派类任务-拒绝请求体
     * @return Resp
     */
    Resp<Object> rejectTask(RejectTaskReq rejectTaskReq);

    /**
     * 查看拒绝原因
     * @param rejectTaskReasonReq 指派类任务-查看拒绝原因请求体
     * @return Resp
     */
    Resp<Object> getRejectReason(RejectTaskReasonReq rejectTaskReasonReq);


    /**
     * 任务接收人清单
     * @param id 任务id
     * @return Resp
     */
    Resp<List<ReceiverListResp>> getTaskCheckList(Long id, Integer taskType);

    /**
     * @param id
     * @return
     */
    Resp<Object> getSkillTaskProgress(Long id);


    /**
     * @param id
     * @return
     */
    Resp<Object> getAssignTaskProgress(Long id);

//    /**
//     * @param id
//     * @return
//     */
//    Resp<Object> getAssignTaskDetailReleaser(Long id);
//
//    /**
//     * @param id
//     * @return
//     */
//    Resp<Object> getSkillTaskDetailReleaser(Long id);
//
//    /**
//     * @param id
//     * @return
//     */
//    Resp<Object> getAssignTaskDetailAcceptor(Long id);
//
//    /**
//     * @param id
//     * @return
//     */
//    Resp<Object> getSkillTaskDetailAcceptor(Long id);

    Resp<Object> getTaskDetails(Long taskId, Integer taskType);
}
