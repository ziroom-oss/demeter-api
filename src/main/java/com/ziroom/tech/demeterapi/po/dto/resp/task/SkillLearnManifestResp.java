package com.ziroom.tech.demeterapi.po.dto.resp.task;

import lombok.Data;

import java.util.Date;

/**
 * @author lipp3
 * @date 2021/6/30 17:51
 * @Description
 */
@Data
public class SkillLearnManifestResp {

    private Long id;

    private String name;

    private String assignerUid;

    private String learnerUid;

    private String learnPeriod;

    private Integer status;

    //学习清单技能奖励值
    private Integer reward;

    private Date createTime;

    private Date modifyTime;

    private String createId;

    private String modifyId;
}
