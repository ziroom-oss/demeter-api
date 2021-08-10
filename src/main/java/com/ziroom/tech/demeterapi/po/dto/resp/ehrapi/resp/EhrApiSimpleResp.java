package com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.resp;

import java.util.List;
import lombok.Data;

/**
 * @author daijiankun
 */
@Data
public class EhrApiSimpleResp {

    private String empCode;

    private String empName;

    private String sex;

    private String adCode;

    private String email;

    private String phone;

    private Integer status;

    private String empType;

    private List<EhrApiJobs> jobs;

}
