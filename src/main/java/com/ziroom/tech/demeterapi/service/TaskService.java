package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.task.CreateAssignReq;
import com.ziroom.tech.demeterapi.po.dto.req.task.CreateSkillReq;

/**
 * @author daijiankun
 */
public interface TaskService {

    /**
     * 创建指派类任务
     * @param createAssignReq
     * @return
     */
    Resp createAssignTask(CreateAssignReq createAssignReq);

    /**
     * 创建技能类任务
     * @param createSkillReq
     * @return
     */
    Resp createSkillTask(CreateSkillReq createSkillReq);
}
