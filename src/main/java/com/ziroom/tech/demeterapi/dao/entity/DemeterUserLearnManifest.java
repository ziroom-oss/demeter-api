package com.ziroom.tech.demeterapi.dao.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;


@Builder
@Data
public class DemeterUserLearnManifest {

    private Long id;

    private String name;

    private String assignerUid;

    private String learnerUid;

    private Date learnPeriodStart;

    private Date learnPeriodEnd;

    private Date createTime;

    private Date modifyTime;

    private String createId;

    private String modifyId;
}