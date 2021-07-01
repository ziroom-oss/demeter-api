package com.ziroom.tech.demeterapi.po.dto.resp.storage;

import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class ZiroomFile {

    private String uuid;

    private String original_filename;

    private String url;

    private String domain;

    private String url_base;

    private String url_ext;

    private String tags;

    private String desc;

    private String active_time;
}