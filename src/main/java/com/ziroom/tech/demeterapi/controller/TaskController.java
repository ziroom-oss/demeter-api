package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.task.AssignTaskReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.RejectTaskReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.SkillTaskReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.TaskListQueryReq;
import com.ziroom.tech.demeterapi.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
    public Resp createAssignTask(@RequestBody AssignTaskReq assignTaskReq) {
        return taskService.createAssignTask(assignTaskReq);
    }

    @PostMapping("save/skill")
    @ApiOperation(value = "新建技能类任务", httpMethod = "POST")
    public Resp createSkillTask(@RequestBody SkillTaskReq skillTaskReq) {
        return taskService.createSkillTask(skillTaskReq);
    }

    @PostMapping("update/assign")
    @ApiOperation(value = "编辑指派类任务", httpMethod = "POST")
    public Resp updateAssignTask(@RequestBody AssignTaskReq assignTaskReq) {
        return taskService.updateAssignTask(assignTaskReq);
    }

    @PostMapping("update/skill")
    @ApiOperation(value = "编辑技能类任务", httpMethod = "POST")
    public Resp updateSkillTask(@RequestBody SkillTaskReq skillTaskReq) {
        return taskService.updateSkillTask(skillTaskReq);
    }

    @PostMapping("/list/release")
    @ApiOperation(value = "任务列表-我发布的", httpMethod = "POST")
    public Resp getReleaseList(@RequestBody TaskListQueryReq taskListQueryReq) {
        return taskService.getReleaseList(taskListQueryReq);
    }

    @PostMapping("/list/execute")
    @ApiOperation(value = "任务列表-我接收的", httpMethod = "POST")
    public Resp getExecuteList(@RequestBody TaskListQueryReq taskListQueryReq) {
        return taskService.getExecuteList(taskListQueryReq);
    }

    @PostMapping("/detail/assign")
    @ApiOperation(value = "任务详情-指派类任务", httpMethod = "POST")
    public Resp getAssignTaskDetail(@RequestParam Long id) {
        return taskService.getAssignDetail(id);
    }

    @PostMapping("/detail/skill")
    @ApiOperation(value = "任务详情-技能类任务", httpMethod = "POST")
    public Resp getSkillTaskDetail(@RequestParam Long id) {
        return taskService.getSkillDetail(id);
    }

    @PostMapping("/task/delete")
    @ApiOperation(value = "任务删除", httpMethod = "POST")
    public Resp deleteTask(@RequestParam Long id, @RequestParam Integer type) {
        return taskService.delete(id, type);
    }

    @PostMapping("/task/accept")
    @ApiOperation(value = "任务认领", httpMethod = "POST")
    public Resp acceptTask(@RequestParam Long id, @RequestParam Integer type) {
        return taskService.acceptTask(id, type);
    }

    // todo test
    @PostMapping("/task/reject")
    @ApiOperation(value = "任务拒绝", httpMethod = "POST")
    public Resp acceptTask(@RequestBody RejectTaskReq rejectTaskReq) {
        return taskService.rejectTask(rejectTaskReq);
    }

    @PostMapping("/assign/detail/check")
    @ApiOperation(value = "指派类任务验收清单", httpMethod = "POST")
    public Resp getAssignTaskCheckList(Long id) {
        return taskService.getAssignTaskCheckList(id);
    }

    @PostMapping("/skill/detail/auth")
    @ApiOperation(value = "技能类任务认证清单", httpMethod = "POST")
    public Resp getSkillTaskAuthList(Long id) {
        return taskService.getSkillTaskAuthList(id);
    }


}
