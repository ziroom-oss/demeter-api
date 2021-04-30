package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.task.*;
import com.ziroom.tech.demeterapi.po.dto.resp.task.ReceiverListResp;
import com.ziroom.tech.demeterapi.po.dto.resp.task.ReleaseQueryResp;
import com.ziroom.tech.demeterapi.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author daijiankun
 */
@Api(tags = "任务相关")
@Slf4j
@RestController
@RequestMapping("api/task")
public class TaskController {

    @Resource
    private TaskService taskService;

    @PostMapping("save/assign")
    @ApiOperation(value = "新建指派类任务", httpMethod = "POST")
    public Resp<Object> createAssignTask(@RequestBody AssignTaskReq assignTaskReq) {
        return taskService.createAssignTask(assignTaskReq);
    }

    @PostMapping("save/skill")
    @ApiOperation(value = "新建技能类任务", httpMethod = "POST")
    public Resp<Object> createSkillTask(@RequestBody SkillTaskReq skillTaskReq) {
        return taskService.createSkillTask(skillTaskReq);
    }

    @PostMapping("update/assign")
    @ApiOperation(value = "编辑指派类任务", httpMethod = "POST")
    public Resp<Object> updateAssignTask(@RequestBody AssignTaskReq assignTaskReq) {
        return taskService.updateAssignTask(assignTaskReq);
    }

    @PostMapping("update/skill")
    @ApiOperation(value = "编辑技能类任务", httpMethod = "POST")
    public Resp<Object> updateSkillTask(@RequestBody SkillTaskReq skillTaskReq) {
        return taskService.updateSkillTask(skillTaskReq);
    }

    @PostMapping("/list/release")
    @ApiOperation(value = "任务列表-我发布的", httpMethod = "POST")
    public Resp<List<ReleaseQueryResp>> getReleaseList(@RequestBody TaskListQueryReq taskListQueryReq) {
        return taskService.getReleaseList(taskListQueryReq);
    }

    @PostMapping("/list/execute")
    @ApiOperation(value = "任务列表-我接收的", httpMethod = "POST")
    public Resp<Object> getExecuteList(@RequestBody TaskListQueryReq taskListQueryReq) {
        return taskService.getExecuteList(taskListQueryReq);
    }

//    @PostMapping("/detail/assign/publisher")
//    @ApiOperation(value = "任务详情-指派类任务-发布者", httpMethod = "POST")
//    public Resp<Object> getAssignTaskDetailReleaser(@RequestParam Long id) {
//        return taskService.getAssignTaskDetailReleaser(id);
//    }
//
//    @PostMapping("/detail/skill/publisher")
//    @ApiOperation(value = "任务详情-技能类任务-发布者", httpMethod = "POST")
//    public Resp<Object> getSkillTaskDetailReleaser(@RequestParam Long id) {
//        return taskService.getSkillTaskDetailReleaser(id);
//    }
//
//    @PostMapping("/detail/assign/receiver")
//    @ApiOperation(value = "任务详情-指派类任务-接收人", httpMethod = "POST")
//    public Resp<Object> getAssignTaskDetailAcceptor(@RequestParam Long id) {
//        return taskService.getAssignTaskDetailAcceptor(id);
//    }
//
//    @PostMapping("/detail/skill/receiver")
//    @ApiOperation(value = "任务详情-技能类任务-接收人", httpMethod = "POST")
//    public Resp<Object> getSkillTaskDetailAcceptor(@RequestParam Long id) {
//        return taskService.getSkillTaskDetailAcceptor(id);
//    }


    @PostMapping("/detail")
    @ApiOperation(value = "任务详情", httpMethod = "POST")
    public Resp<Object> getTaskDetails(@RequestParam Long taskId, @RequestParam Integer taskType) {
        return taskService.getTaskDetails(taskId, taskType);
    }


    @PostMapping("/task/delete")
    @ApiOperation(value = "任务删除", httpMethod = "POST")
    public Resp<Object> deleteTask(@RequestParam Long id, @RequestParam Integer type) {
        return taskService.delete(id, type);
    }

    @PostMapping("/task/accept")
    @ApiOperation(value = "任务认领", httpMethod = "POST")
    public Resp<Object> acceptTask(@RequestParam Long id, @RequestParam Integer type) {
        return taskService.acceptTask(id, type);
    }

    // todo test
    @PostMapping("/task/reject")
    @ApiOperation(value = "任务拒绝", httpMethod = "POST")
    public Resp<Object> acceptTask(@RequestBody RejectTaskReq rejectTaskReq) {
        return taskService.rejectTask(rejectTaskReq);
    }

    @PostMapping("/task/reject/reason")
    @ApiOperation(value = "查看拒绝原因", httpMethod = "POST")
    public Resp<Object> acceptTask(@RequestBody RejectTaskReasonReq rejectTaskReasonReq) {
        return taskService.getRejectReason(rejectTaskReasonReq);
    }

    @PostMapping("/task/auth")
    @ApiOperation(value = "任务验收", httpMethod = "POST")
    public Resp<Object> checkTask(@RequestBody CheckTaskReq checkTaskReq) {
        return taskService.checkTask(checkTaskReq);
    }

    @PostMapping("/assign/detail/check")
    @ApiOperation(value = "两类任务-查看接收人清单", httpMethod = "POST")
    public Resp<List<ReceiverListResp>> getAssignTaskCheckList(@RequestParam Long taskId, @RequestParam Integer taskType) {
        return taskService.getTaskCheckList(taskId, taskType);
    }

    @PostMapping("/skill/detail/progress")
    @ApiOperation(value = "技能类任务查看进度", httpMethod = "POST")
    public Resp<Object> getSkillTaskProgress(@RequestParam Long id, @RequestParam String uid) {
        return taskService.getSkillTaskProgress(id);
    }

    @PostMapping("/assign/detail/progress")
    @ApiOperation(value = "指派类任务查看进度", httpMethod = "POST")
    public Resp<Object> getAssignTaskProgress(@RequestParam Long id, @RequestParam String uid) {
        return taskService.getAssignTaskProgress(id);
    }


    @PostMapping("/graph")
    @ApiOperation(value = "查看任务关联的知识图谱", httpMethod = "POST")
    public Resp<Object> getSkillGraphOfTask(@RequestParam Long id, @RequestParam Integer taskType) {
        return null;
    }
}
