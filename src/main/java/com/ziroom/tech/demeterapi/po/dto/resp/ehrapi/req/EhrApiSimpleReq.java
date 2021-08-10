package com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.req;

import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * ehr-api接口
 * 根据条件查询员工基本信息
 * 适用于根据系统号或者邮箱前缀或者手机号查员工基础的信息，只返回一条数据，包含在离职状态
 *
 * @author daijk
 **/
@Data
public class EhrApiSimpleReq {

    /**
     * 邮箱前缀
     */
    private String adCode;

    /**
     * 员工系统号
     */
    private String empCode;

    /**
     * 手机号
     */
    private String phone;

}
