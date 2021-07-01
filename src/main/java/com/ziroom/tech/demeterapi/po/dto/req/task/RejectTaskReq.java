package com.ziroom.tech.demeterapi.po.dto.req.task;

import lombok.Data;

/**
 * 指派类任务-拒绝请求体
 * @author daijiankun
 */
@Data
public class RejectTaskReq {

    private Long taskId;

    private String reason;
}
