package com.ziroom.tech.demeterapi.po.dto.resp.portrait;

import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class PersonalDeployResp {

    private Integer deploymentNum;

    private Integer restartNum;

    private Integer rollbackNum;

    private Integer onlineNum;

    private Integer ciNum;
}
