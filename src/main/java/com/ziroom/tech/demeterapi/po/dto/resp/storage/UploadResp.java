package com.ziroom.tech.demeterapi.po.dto.resp.storage;

import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class UploadResp {

    private String server_machine_name;

    private String server_ip;

    private String server_current_time;

    private String response_code;

    private String message_info;

    private String error_info;

    private ZiroomFile file;

    private Object porn;

    private Object face;
}
