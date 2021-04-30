package com.ziroom.tech.demeterapi.po.dto.resp.task;

import lombok.Data;

import java.util.Date;

/**
 * @author daijiankun
 */
@Data
public class SkillDetailResp {

    private Long id;

    private String taskNo;

    private String taskName;

    private Integer taskStatus;

    private String taskStatusName;

    private Integer skillReward;

    private String attachmentUrl;

    private String publisher;

    private String publisherName;

    private String receiverName;

    private String taskRemark;

    private String taskTypeName;

    private Date createTime;

    private Date modifyTime;

    private String createId;

    private String modifyId;
}
