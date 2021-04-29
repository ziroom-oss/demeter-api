package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.task.AssignTaskReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.RejectTaskReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.TaskListQueryReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.SkillTaskReq;
import com.ziroom.tech.demeterapi.po.dto.resp.task.AssignDetailResp;
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
    Resp createAssignTask(AssignTaskReq assignTaskReq);

    /**
     * 创建技能类任务
     * @param skillTaskReq 技能类任务请求体
     * @return Resp
     */
    Resp createSkillTask(SkillTaskReq skillTaskReq);

    /**
     * 编辑指派类任务
     * @param assignTaskReq 指派类任务请求体
     * @return Resp
     */
    Resp updateAssignTask(AssignTaskReq assignTaskReq);

    /**
     * 编辑技能类任务
     * @param skillTaskReq  技能类任务请求体
     * @return Resp
     */
    Resp updateSkillTask(SkillTaskReq skillTaskReq);

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
    Resp getExecuteList(TaskListQueryReq taskListQueryReq);

    /**
     * 任务详情-指派类任务
     * @param id 任务id
     * @return Resp<AssignDetailResp>
     */
    Resp<AssignDetailResp> getAssignDetail(Long id);

    /**
     * 任务详情-技能类任务
     * @param id 任务id
     * @return Resp
     */
    Resp getSkillDetail(Long id);

    /**
     * 删除任务
     * @param id 任务id
     * @param taskType 任务类型
     * @return Resp
     */
    Resp delete(Long id, Integer taskType);

    /**
     * 认领任务
     * @param id 任务id
     * @param type 任务类型
     * @return Resp
     */
    Resp acceptTask(Long id, Integer type);

    /**
     * 拒绝指派类任务
     * @param rejectTaskReq 指派类任务-拒绝请求体
     * @return Resp
     */
    Resp rejectTask(RejectTaskReq rejectTaskReq);


    /**
     * 指派类任务验收清单
     * @param id 任务id
     * @return Resp
     */
    Resp getAssignTaskCheckList(Long id);

    /**
     * 技能类任务认证清单
     * @param id 任务id
     * @return Resp
     */
    Resp getSkillTaskAuthList(Long id);
}
