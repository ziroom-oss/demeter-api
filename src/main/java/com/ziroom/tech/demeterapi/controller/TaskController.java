package com.ziroom.tech.demeterapi.controller;

import com.google.common.base.Preconditions;
import com.ziroom.tech.demeterapi.common.PageListResp;
import com.ziroom.tech.demeterapi.common.enums.*;
import com.ziroom.tech.demeterapi.dao.entity.DemeterSkillTask;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.task.*;
import com.ziroom.tech.demeterapi.po.dto.resp.task.*;
import com.ziroom.tech.demeterapi.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "save/assign", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "新建指派类任务", httpMethod = "POST")
    public Resp<Object> createAssignTask(AssignTaskReq assignTaskReq) {
        assignTaskReq.validateAdd();
        return taskService.createAssignTask(assignTaskReq);
    }

    @PostMapping(value = "save/skill", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "新建技能类任务", httpMethod = "POST")
    public Resp<Object> createSkillTask(SkillTaskReq skillTaskReq) {
        skillTaskReq.validateAdd();
        return taskService.createSkillTask(skillTaskReq);
    }

    @PostMapping("get/assign")
    @ApiOperation(value = "查看指派类任务", httpMethod = "POST")
    public Resp<AssignDetailResp> getAssignTask(@RequestParam Long id) {
        return taskService.getAssignTask(id);
    }

    @PostMapping("get/skill")
    @ApiOperation(value = "查看技能类任务", httpMethod = "POST")
    public Resp<SkillDetailResp> getSkillTask(@RequestParam Long id) {
        return taskService.getSkillTask(id);
    }

    @GetMapping("type/all")
    public Resp<Object> getAllTaskTypes() {
        return Resp.success(TaskType.getAllTaskType());
    }

    @GetMapping("status/assign")
    public Resp<Object> getAllAssignStatus() {
        return Resp.success(AssignTaskStatus.getAllTaskType());
    }

    @GetMapping("status/skill")
    public Resp<Object> getAllSkillStatus() {
        return Resp.success(SkillTaskStatus.getAllTaskType());
    }

    @GetMapping("status/flow/assign")
    public Resp<Object> getAllAssignFlowStatus() {
        return Resp.success(AssignTaskFlowStatus.getAllTaskType());
    }

    @GetMapping("status/flow/skill")
    public Resp<Object> getAllSkillFlowStatus() {
        return Resp.success(SkillTaskFlowStatus.getAllTaskType());
    }

    @GetMapping("skill/level")
    public Resp<Object> getAllSkillLevel() {
        return Resp.success(SkillPointLevel.getAllSkillLevel());
    }

    // @daijr
    @PostMapping("skill/move")
    public Resp<Object> submitSkillMove(@RequestParam Long id, @RequestParam Long skillTreeId) {
        return Resp.success(taskService.submitSkillMove(id, skillTreeId));
    }

    @PostMapping(value = "update/assign", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "编辑指派类任务", httpMethod = "POST")
    public Resp<Object> updateAssignTask(AssignTaskReq assignTaskReq) {
        assignTaskReq.validateAdd();
        return taskService.updateAssignTask(assignTaskReq);
    }

    @PostMapping(value = "update/skill", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "编辑技能类任务", httpMethod = "POST")
    public Resp<Object> updateSkillTask(SkillTaskReq skillTaskReq) {
        return taskService.updateSkillTask(skillTaskReq);
    }

    @PostMapping("status/assign")
    @ApiOperation(value = "任务自身状态更改：指派类：开启 关闭，技能类：启用 禁用", httpMethod = "POST")
    public Resp<Object> updateAssignTaskStatus(@RequestParam Long taskId, Integer taskType, Integer taskStatus) {
        return taskService.updateAssignTaskStatus(taskId, taskType, taskStatus);
    }

    @PostMapping("/list/release")
    @ApiOperation(value = "发布任务列表", httpMethod = "POST")
    public Resp<PageListResp<ReleaseQueryResp>> getReleaseList(@RequestBody TaskListQueryReq taskListQueryReq) {
        taskListQueryReq.validate();
        return Resp.success(taskService.getReleaseList(taskListQueryReq));
    }

    @PostMapping("/list/receive")
    @ApiOperation(value = "接收任务列表", httpMethod = "POST")
    public Resp<PageListResp<ReceiveQueryResp>> getExecuteList(@RequestBody TaskListQueryReq taskListQueryReq) {
        taskListQueryReq.validate();
        return Resp.success(taskService.getExecuteList(taskListQueryReq));
    }

    @PostMapping("/detail/all")
    @ApiOperation(value = "任务详情", httpMethod = "POST")
    public Resp<TaskDetailResp> getAllDetails(@RequestParam Long taskId, @RequestParam Integer taskType) {
        return taskService.getAllDetails(taskId, taskType);
    }

    @PostMapping("/task/delete")
    @ApiOperation(value = "任务删除", httpMethod = "POST")
    public Resp<Object> deleteTask(@RequestParam Long id, @RequestParam Integer type) {
        return taskService.delete(id, type);
    }

    @PostMapping("/assign/re-designate")
    @ApiOperation(value = "任务再次指派", httpMethod = "POST")
    public Resp<Object> reDesignateTask(@RequestParam Long id, @RequestParam Integer type) {
        return null;
    }

    @PostMapping("/accept")
    @ApiOperation(value = "任务认领", httpMethod = "POST")
    public Resp<Object> acceptTask(@RequestParam Long id, @RequestParam Integer type) {
        return taskService.acceptTask(id, type);
    }

    @PostMapping("/condition/finish")
    @ApiOperation(value = "任务条件完成", httpMethod = "POST")
    public Resp<Object> finishTaskCondition(@RequestParam("id") Long conditionInfoId) {
        return taskService.finishTaskCondition(conditionInfoId);
    }

    // todo test
    @PostMapping("/reject")
    @ApiOperation(value = "任务拒绝", httpMethod = "POST")
    public Resp<Object> rejectTask(@RequestBody RejectTaskReq rejectTaskReq) {
        return taskService.rejectTask(rejectTaskReq);
    }

    @PostMapping("/reject/reason")
    @ApiOperation(value = "查看拒绝原因", httpMethod = "POST")
    public Resp<Object> getRejectReason(@RequestBody RejectTaskReasonReq rejectTaskReasonReq) {
        return Resp.success(taskService.getRejectReason(rejectTaskReasonReq));
    }

    @PostMapping("/reassign")
    @ApiOperation(value = "任务重新指派", httpMethod = "POST")
    public Resp<Object> reassignTask(@RequestBody ReassignTaskReq reassignTaskReq) {
        return Resp.success(taskService.reassignTask(reassignTaskReq));
    }

    @PostMapping("/submit/auth")
    @ApiOperation(value = "任务提交验收", httpMethod = "POST")
    public Resp<Object> submitCheckTask(@RequestParam Long taskId, @RequestParam Integer taskType) {
        return taskService.submitCheckTask(taskId, taskType);
    }

    @PostMapping("/submit/complete")
    @ApiOperation(value = "任务直接完成", httpMethod = "POST")
    public Resp<Object> submitComplete(@RequestParam Long taskId) {
        return taskService.submitComplete(taskId);
    }

    @PostMapping("/auth")
    @ApiOperation(value = "任务验收", httpMethod = "POST")
    public Resp<Object> checkTask(@RequestBody CheckTaskReq checkTaskReq) {
        return taskService.checkTask(checkTaskReq);
    }

    // todo test
    @PostMapping("/detail/progress")
    @ApiOperation(value = "任务查看进度/验收任务预览", httpMethod = "POST")
    public Resp<TaskProgressResp> getAssignTaskProgress(@RequestParam(value = "id") Long id) {
        return taskService.getTaskProgress(id);
    }

    @PostMapping("/graph")
    @ApiOperation(value = "查看任务关联的知识图谱", httpMethod = "POST")
    public Resp<Object> getSkillGraphOfTask(@RequestParam Long id, @RequestParam Integer taskType) {
        return null;
    }

    @PostMapping(value = "/upload/outcome", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "上传学习成果", httpMethod = "POST")
    public Resp<Object> uploadLearningOutcome(UploadOutcomeReq uploadOutcomeReq) {
        return taskService.uploadLearningOutcome(uploadOutcomeReq);
    }

    @PostMapping("/graph/search")
    @ApiOperation(value = "技能任务搜索，用来关联技能图谱", httpMethod = "POST")
    public Resp<List<DemeterSkillTask>> searchSkillForGraph(@RequestParam String condition) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(condition), "搜索条件不能为空");
        return Resp.success(taskService.searchSkillForGraph(condition));
    }

}
