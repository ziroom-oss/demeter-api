package com.ziroom.tech.demeterapi.po.dto.req.portrayal;

import java.util.Date;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class PersonReq {

    private String uid;

    private Date startTime;

    private Date endTime;
}
