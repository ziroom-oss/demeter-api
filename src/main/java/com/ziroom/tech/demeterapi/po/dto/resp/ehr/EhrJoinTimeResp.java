package com.ziroom.tech.demeterapi.po.dto.resp.ehr;

import lombok.Data;

/**
 * 查询ehr入职时间
 * @author daijiankun
 */
@Data
public class EhrJoinTimeResp {

    private String empCode;

    private String entryTime;
}
