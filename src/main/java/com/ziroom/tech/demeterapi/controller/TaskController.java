package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.task.AssignTaskReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.SkillTaskReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.TaskListQueryReq;
import com.ziroom.tech.demeterapi.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
        log.info("TaskController.createAssignTask params:{}", assignTaskReq);
        return taskService.createAssignTask(assignTaskReq);
    }

    @PostMapping("save/skill")
    @ApiOperation(value = "新建技能类任务", httpMethod = "POST")
    public Resp createSkillTask(@RequestBody SkillTaskReq skillTaskReq) {
        log.info("TaskController.createSkillTask params:{}", skillTaskReq);
        return taskService.createSkillTask(skillTaskReq);
    }

    @PostMapping("update/assign")
    @ApiOperation(value = "编辑指派类任务", httpMethod = "POST")
    public Resp updateAssignTask(@RequestBody AssignTaskReq assignTaskReq) {
        log.info("TaskController.createAssignTask params:{}", assignTaskReq);
        return taskService.updateAssignTask(assignTaskReq);
    }

    @PostMapping("update/skill")
    @ApiOperation(value = "编辑技能类任务", httpMethod = "POST")
    public Resp updateSkillTask(@RequestBody SkillTaskReq skillTaskReq) {
        log.info("TaskController.updateSkillTask params:{}", skillTaskReq);
        return taskService.updateSkillTask(skillTaskReq);
    }

    @PostMapping("/list/release")
    @ApiOperation(value = "任务列表-我发布的", httpMethod = "POST")
    public Resp getReleaseList(@RequestBody TaskListQueryReq taskListQueryReq) {
        log.info("TaskController.getReleaseList params:{}", taskListQueryReq);
        return taskService.getReleaseList(taskListQueryReq);
    }

    @PostMapping("/list/execute")
    @ApiOperation(value = "任务列表-我接收的", httpMethod = "POST")
    public Resp getExecuteList(@RequestBody TaskListQueryReq taskListQueryReq) {
        log.info("TaskController.getReleaseList params:{}", taskListQueryReq);
        return taskService.getExecuteList(taskListQueryReq);
    }

    @PostMapping("/detail/assign")
    @ApiOperation(value = "任务详情-指派类任务", httpMethod = "POST")
    public Resp getAssignTaskDetail(@RequestParam Long id) {
        log.info("TaskController.getAssignTaskDetail params:{}", id);
        return taskService.getAssignDetail(id);
    }

    @PostMapping("/detail/skill")
    @ApiOperation(value = "任务详情-技能类任务", httpMethod = "POST")
    public Resp getSkillTaskDetail(@RequestParam Long id) {
        log.info("TaskController.getAssignTaskDetail params:{}", id);
        return taskService.getSkillDetail(id);
    }

    @PostMapping("/task/delete")
    @ApiOperation(value = "任务删除", httpMethod = "POST")
    public Resp deleteTask(@RequestParam Long id, @RequestParam Integer type) {
        log.info("TaskController.deleteTask params: id = {}, type = {}", id, type);
        return taskService.delete(id, type);
    }
}
