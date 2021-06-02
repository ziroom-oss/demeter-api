package com.ziroom.tech.demeterapi.po.dto.req.storage;

import lombok.Builder;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
@Builder
public class DeleteParam {

    private String source;

    private String uuid;
}
