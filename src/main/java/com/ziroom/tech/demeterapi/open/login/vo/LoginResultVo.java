package com.ziroom.tech.demeterapi.open.login.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: xuzeyu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResultVo implements Serializable {

    /**
     * 用户code
     */
    private String uid;

    /**
     * 姓名
     */
    private String name;

    /**
     * token
     */
    private String token ;

}
