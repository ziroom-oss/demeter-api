package com.ziroom.tech.demeterapi.po.dto.req.role;

import java.util.List;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class RoleUserReq {

    private Long roleId;

    private List<String> systemCodeList;
}
