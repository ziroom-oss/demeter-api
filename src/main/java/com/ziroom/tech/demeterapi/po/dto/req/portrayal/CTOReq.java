package com.ziroom.tech.demeterapi.po.dto.req.portrayal;

import java.util.Date;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class CTOReq {

    private String deptId;

    private String adCode;

    private Date startDate;

    private Date endDate;
}
