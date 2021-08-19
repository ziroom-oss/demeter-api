package com.ziroom.tech.demeterapi.po.vo;

import java.util.Date;
import lombok.Data;

@Data
public class LearnManifestVo {

    private Long id;

    private String name;

    private String assignerUid;

    private String assignerName;

    private String learnerUid;

    private Date learnPeriodStart;

    private Date learnPeriodEnd;

    private Date createTime;

    private Date modifyTime;

    private String createId;

    private String modifyId;
}