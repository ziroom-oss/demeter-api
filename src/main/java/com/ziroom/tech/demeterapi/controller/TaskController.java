package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.task.*;
import com.ziroom.tech.demeterapi.po.dto.resp.task.ReceiverListResp;
import com.ziroom.tech.demeterapi.po.dto.resp.task.ReleaseQueryResp;
import com.ziroom.tech.demeterapi.po.dto.resp.task.TaskProgressResp;
import com.ziroom.tech.demeterapi.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
    public Resp<Object> acceptTask(@RequestBody RejectTaskReq rejectTaskReq) {
        return taskService.rejectTask(rejectTaskReq);
    }

    @PostMapping("/reject/reason")
    @ApiOperation(value = "查看拒绝原因", httpMethod = "POST")
    public Resp<Object> acceptTask(@RequestBody RejectTaskReasonReq rejectTaskReasonReq) {
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

    @PostMapping("/receiver/list")
    @ApiOperation(value = "两类任务-查看接收人清单", httpMethod = "POST")
    public Resp<List<ReceiverListResp>> getAssignTaskCheckList(@RequestParam Long taskId, @RequestParam Integer taskType) {
        return taskService.getTaskCheckList(taskId, taskType);
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
