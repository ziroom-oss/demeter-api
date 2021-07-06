package com.ziroom.tech.demeterapi.po.dto.req.worktop;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
@Builder
public class PersonalReq {

    private String adCode;

    private Date begin;

    private Date end;
}
