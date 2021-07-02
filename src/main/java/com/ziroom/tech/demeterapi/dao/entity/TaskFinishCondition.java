package com.ziroom.tech.demeterapi.dao.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author daijiankun
 */
@Builder
@Data
public class TaskFinishCondition {

    private Long id;

    private Long taskId;

    private String taskFinishContent;

    private Integer taskType;

    private Date createTime;

    private Date modifyTime;

    private String createId;

    private String modifyId;
}