package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.task.CreateAssignReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.CreateSkillReq;
import com.ziroom.tech.demeterapi.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Resp createAssignTask(CreateAssignReq createAssignReq) {
        log.info("TaskController.createAssignTask params:{}", createAssignReq);
        return taskService.createAssignTask(createAssignReq);
    }

    @PostMapping("save/skill")
    @ApiOperation(value = "新建技能类任务", httpMethod = "POST")
    public Resp createSkillTask(CreateSkillReq createSkillReq) {
        log.info("TaskController.createSkillTask params:{}", createSkillReq);
        return taskService.createSkillTask(createSkillReq);
    }
}
