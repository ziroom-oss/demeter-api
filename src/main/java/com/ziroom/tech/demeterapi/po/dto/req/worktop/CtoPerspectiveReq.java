package com.ziroom.tech.demeterapi.po.dto.req.worktop;

import java.util.Date;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class CtoPerspectiveReq {

    private String deptId;

    private Date begin;

    private Date end;
}
