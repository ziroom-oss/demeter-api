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

    private Long id;//1、学习清单编号

    private String name;//2、学习清单名称

    private String  statusName; //3、状态

    private Date createTime;//4、创建时间

    private Date learnPeriodStart; // 学习周期开始时间

    private Date learnPeriodEnd; // 学习周期结束时间

    //学习清单技能奖励值
    private Integer reward;//5、技能点奖励

    private String assignerName;//6、分配人

    private String learnerName;//7、认领人

    private Integer taskType;

    private String assignerUid;

    private String learnerUid;

    private String learnPeriod;

    private Integer status;

    private Date modifyTime;

    private String createId;

    private String modifyId;
}
