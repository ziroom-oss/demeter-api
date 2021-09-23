package com.ziroom.tech.demeterapi.dao.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class DemeterCoreData {

    private Long id;

    private String coreName;

    private String taskType;

    private String receiverUid;

    private String taskStatus;

    private String rejectReason;

    private Date checkResult;

    private Date checkOpinion;

    private String coreSysName;



}
