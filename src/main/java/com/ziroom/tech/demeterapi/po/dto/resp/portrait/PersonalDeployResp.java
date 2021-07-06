package com.ziroom.tech.demeterapi.po.dto.resp.portrait;

import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class PersonalDeployResp {

    private Integer deploymentNum = 0;

    private Integer restartNum = 0;

    private Integer rollbackNum = 0;

    private Integer onlineNum = 0;

    private Integer ciNum = 0;
}
