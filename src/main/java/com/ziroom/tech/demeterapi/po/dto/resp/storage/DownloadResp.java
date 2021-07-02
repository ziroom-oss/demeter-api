package com.ziroom.tech.demeterapi.po.dto.resp.storage;

import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class DownloadResp {

    private String server_machine_name;

    private String server_ip;

    private String server_current_time;

    private String response_code;

    private String message_info;

    private String error_info;

    private ZiroomFile file;
}
