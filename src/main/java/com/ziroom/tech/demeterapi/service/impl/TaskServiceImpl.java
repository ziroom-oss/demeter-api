package com.ziroom.tech.demeterapi.service.impl;

import com.ziroom.tech.demeterapi.dao.mapper.DemeterAssignTaskDao;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.task.CreateAssignReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.CreateSkillReq;
import com.ziroom.tech.demeterapi.service.TaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author daijiankun
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Resource
    private DemeterAssignTaskDao demeterAssignTaskDao;

    @Override
    public Resp createAssignTask(CreateAssignReq createAssignReq) {
        
        return null;
    }

    @Override
    public Resp createSkillTask(CreateSkillReq createSkillReq) {
        return null;
    }
}
