package com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.resp;

import lombok.Data;

/**
 * ehr接口getEhrDept.action返回结果的封装
 *
 * @author chenx34
 * @date 2020/5/8 12:51
 */
@Data
public class EhrEmpDeptResp {

    private String deptName;

    private String email;

    private String jobShort;

    private String userCode;


    private String phone;

    private String job;

    private String userName;

    private String deptGenericName;
}
