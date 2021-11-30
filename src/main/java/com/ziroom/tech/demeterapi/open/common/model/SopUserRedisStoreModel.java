package com.ziroom.tech.demeterapi.open.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: xuzeyu
 */
@Data
@NoArgsConstructor
public class SopUserRedisStoreModel implements Serializable {

    /**
     * 用户code 唯一标识
     */
    private String userCode;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 续签次数
     */
    private Long renewTimes ;
    /**
     * 续签时间
     */
    private Long renewTimeStamp ;

}
