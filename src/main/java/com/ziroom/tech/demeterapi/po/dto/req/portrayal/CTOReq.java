package com.ziroom.tech.demeterapi.po.dto.req.portrayal;

import java.util.Date;
import java.util.List;
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

    /**
     * 环比 false，同比 true
     */
    private Boolean basePeriod;

    private String subDeptId;

    private List<String> selectUserCode;
}
