package com.ziroom.tech.demeterapi.po.dto.req.task;

import lombok.Data;

import java.util.List;

/**
 * 指派类任务-重新指派请求体
 * @author daijiankun
 */
@Data
public class ReassignTaskReq {

    /**
     * 任务id
     */
    private Long id;

    private List<String> reassignList;
}
