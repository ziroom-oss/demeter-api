package com.ziroom.tech.demeterapi.open.auth.param;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author xuzeyu
 **/
@Data
@NoArgsConstructor
public class AuthReqParam {

    /**
     * 应用id
     */
    private String appId;

    /**
     * 用户code
     */
    private String userCode;

}
