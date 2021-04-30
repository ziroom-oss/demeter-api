package com.ziroom.tech.demeterapi.po.dto.req.task;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 验收任务请求体
 * @author daijiankun
 */
@Data
public class CheckTaskReq {

    private Long id;

    /**
     * 接收者系统号
     */
    private String receiverUid;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 任务类型
     */
    private Integer taskType;

    /**
     * 验收结果：0 不通过，1 通过
     */
    private String result;

    /**
     * 验收意见
     */
    private String acceptanceOpinion;
}
