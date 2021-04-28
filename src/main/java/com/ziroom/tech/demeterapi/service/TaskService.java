package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.task.AssignTaskReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.TaskListQueryReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.SkillTaskReq;

/**
 * @author daijiankun
 */
public interface TaskService {

    /**
     * 创建指派类任务
     * @param assignTaskReq
     * @return
     */
    Resp createAssignTask(AssignTaskReq assignTaskReq);

    /**
     * 创建技能类任务
     * @param skillTaskReq
     * @return
     */
    Resp createSkillTask(SkillTaskReq skillTaskReq);

    /**
     * 编辑指派类任务
     * @param assignTaskReq
     * @return
     */
    Resp updateAssignTask(AssignTaskReq assignTaskReq);

    /**
     * 编辑技能类任务
     * @param skillTaskReq
     * @return
     */
    Resp updateSkillTask(SkillTaskReq skillTaskReq);

    /**
     * 任务列表-我发布的
     * @param taskListQueryReq
     * @return
     */
    Resp getReleaseList(TaskListQueryReq taskListQueryReq);

    /**
     * 任务列表-我接收的
     * @return
     */
    Resp getExecuteList(TaskListQueryReq taskListQueryReq);

    /**
     * 任务详情-指派类任务
     * @return
     */
    Resp getAssignDetail(Long id);

    /**
     * 任务详情-技能类任务
     * @return
     */
    Resp getSkillDetail(Long id);

    Resp delete(Long id, Integer taskType);
}
