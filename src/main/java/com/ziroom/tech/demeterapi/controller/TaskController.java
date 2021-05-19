package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.common.PageListResp;
import com.ziroom.tech.demeterapi.common.enums.AssignTaskFlowStatus;
import com.ziroom.tech.demeterapi.common.enums.SkillTaskFlowStatus;
import com.ziroom.tech.demeterapi.common.enums.TaskType;
import com.ziroom.tech.demeterapi.dao.entity.DemeterAssignTask;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.task.*;
import com.ziroom.tech.demeterapi.po.dto.resp.task.*;
import com.ziroom.tech.demeterapi.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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
        assignTaskReq.validateAdd();
        return taskService.createAssignTask(assignTaskReq);
    }

    @PostMapping("save/skill")
    @ApiOperation(value = "新建技能类任务", httpMethod = "POST")
    public Resp<Object> createSkillTask(@RequestBody SkillTaskReq skillTaskReq) {
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
        return Resp.success(AssignTaskFlowStatus.getAllTaskType());
    }

    @GetMapping("status/skill")
    public Resp<Object> getAllSkillStatus() {
        return Resp.success(SkillTaskFlowStatus.getAllTaskType());
    }

    @PostMapping("update/assign")
    @ApiOperation(value = "编辑指派类任务", httpMethod = "POST")
    public Resp<Object> updateAssignTask(@RequestBody AssignTaskReq assignTaskReq) {
        return taskService.updateAssignTask(assignTaskReq);
    }

    @PostMapping("status/assign")
    @ApiOperation(value = "任务自身状态更改：指派类：开启 关闭，技能类：启用 禁用", httpMethod = "POST")
    public Resp<Object> updateAssignTaskStatus(@RequestParam Long taskId, Integer taskType, Integer taskStatus) {
        return taskService.updateAssignTaskStatus(taskId, taskType, taskStatus);
    }

    @PostMapping("update/skill")
    @ApiOperation(value = "编辑技能类任务", httpMethod = "POST")
    public Resp<Object> updateSkillTask(@RequestBody SkillTaskReq skillTaskReq) {
        return taskService.updateSkillTask(skillTaskReq);
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

    @PostMapping("/detail")
    @ApiOperation(value = "任务详情", httpMethod = "POST")
    public Resp<Object> getTaskDetails(@RequestParam Long taskId, @RequestParam Integer taskType) {
        return taskService.getTaskDetails(taskId, taskType);
    }

    @PostMapping("/detail/all")
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
        return taskService.getRejectReason(rejectTaskReasonReq);
    }

    @PostMapping("/submit/auth")
    @ApiOperation(value = "任务提交验收", httpMethod = "POST")
    public Resp<Object> submitCheckTask(@RequestParam Long taskId, @RequestParam Integer taskType) {
        return taskService.submitCheckTask(taskId, taskType);
    }

    @PostMapping("/auth")
    @ApiOperation(value = "任务验收", httpMethod = "POST")
    public Resp<Object> checkTask(@RequestBody CheckTaskReq checkTaskReq) {
        return taskService.checkTask(checkTaskReq);
    }

//    @PostMapping("/receiver/list")
//    @ApiOperation(value = "两类任务-查看接收人清单", httpMethod = "POST")
//    public Resp<List<ReceiverListResp>> getAssignTaskCheckList(@RequestParam Long taskId, @RequestParam Integer taskType) {
//        return taskService.getTaskCheckList(taskId, taskType);
//    }

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

    // todo test
    @PostMapping("/upload/attachment")
    @ApiOperation(value = "上传附件", httpMethod = "POST")
    public Resp<Object> uploadAttachment(MultipartFile multipartFile, Long taskId, Integer taskType) {
        if (Objects.isNull(multipartFile) || multipartFile.isEmpty()) {
            return Resp.error("文件为空，请重新上传");
        }
        return taskService.uploadAttachment(multipartFile, taskId, taskType);
    }

    // TODO: 2021/5/6 test
    @PostMapping("/upload/outcome")
    @ApiOperation(value = "上传学习成果", httpMethod = "POST")
    public Resp<Object> uploadLearningOutcome(MultipartFile multipartFile, Long taskId, Integer taskType) {
        if (Objects.isNull(multipartFile) || multipartFile.isEmpty()) {
            return Resp.error("文件为空，请重新上传");
        }
        return taskService.uploadLearningOutcome(multipartFile, taskId, taskType);
    }

    // TODO: 2021/5/6 test
    @PostMapping("/download/file")
    @ApiOperation(value = "查看文件", httpMethod = "POST")
    public Resp<Object> downloadFile(String fileUuid) {
        return taskService.readFile(fileUuid);
    }
}
