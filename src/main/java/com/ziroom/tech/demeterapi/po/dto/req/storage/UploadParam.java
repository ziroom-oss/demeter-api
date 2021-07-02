package com.ziroom.tech.demeterapi.po.dto.req.storage;

import lombok.Builder;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
@Builder
public class UploadParam {

    private String source;

    private String filename;

    private String base64;

    private String type;

    private String tags;

    private String desc;

    private String active_time;
}
