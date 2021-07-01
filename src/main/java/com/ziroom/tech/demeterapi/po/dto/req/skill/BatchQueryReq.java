package com.ziroom.tech.demeterapi.po.dto.req.skill;

import java.util.List;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class BatchQueryReq {

    private List<Long> skillIds;

    private String uid;
}
