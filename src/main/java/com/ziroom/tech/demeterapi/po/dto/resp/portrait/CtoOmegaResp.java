package com.ziroom.tech.demeterapi.po.dto.resp.portrait;

import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class CtoOmegaResp {

    /**
     * 发布次数
     */
    private Integer deploymentNum;

    /**
     * 重启次数
     */
    private Integer restartNum;

    /**
     * 回滚次数
     */
    private Integer rollbackNum;

    /**
     * 上线次数
     */
    private Integer onlineNum;

    /**
     * 编译次数
     */
    private Integer ciNum;
}
