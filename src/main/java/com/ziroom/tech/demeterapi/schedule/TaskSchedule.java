package com.ziroom.tech.demeterapi.schedule;

import com.alibaba.fastjson.JSON;
import com.ziroom.tech.demeterapi.service.TaskService;
import com.ziroom.tech.sia.hunter.annotation.OnlineTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author daijiankun
 */
@Slf4j
@RestController
public class TaskSchedule {

    @Resource
    private TaskService taskService;

    @OnlineTask(description = "检查任务是否延期，每天凌晨1点执行一次")
    @RequestMapping(value = "/check/task/delay", method = RequestMethod.POST)
    @CrossOrigin(methods = RequestMethod.POST, origins = "*")
    public String checkTaskDelay(@RequestBody String body) {
        Map<String, String> info = new HashMap<>(16);
        boolean result = taskService.checkTaskDelay();
        info.put("status", result ? "success" : "failure");
        info.put("result", result ? "success" : "failure");
        return JSON.toJSONString(info);
    }
}
