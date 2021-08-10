package com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.resp;

import java.util.Date;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class EhrApiResp<T> {

    private String code;

    private String message;

    private Date timeStamp;

    private Boolean success;

    private T data;
}
