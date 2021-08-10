package com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.resp;

import io.swagger.annotations.ApiModel;
import java.util.Date;
import lombok.Data;

/**
 * okr状态
 *
 * @author huangqiaowei
 * @date 2020-07-02 16:03
 **/
@ApiModel("resp")
@Data
public class NoStatusResp {


    private String userCode;


    private String userName;


    private String cityCode;


    private String cityName;


    private String deptCode;


    private String deptName;

    private String treePath;


    private String treePathName;

    private Date time;

    private String phone;

//    @ExcelProperty("是否离职")
//    private String type;

//    @ExcelProperty("是否产假")
//    private String leave;

    public static NoStatusResp copyByEhrEmpUserResp(EhrEmpUserResp ehrEmpUserResp) {
        NoStatusResp noStatusResp = new NoStatusResp();
        noStatusResp.setUserCode(ehrEmpUserResp.getEmpCode());
        noStatusResp.setUserName(ehrEmpUserResp.getEmpName());
        noStatusResp.setCityCode(ehrEmpUserResp.getCityCode());
        noStatusResp.setCityName(ehrEmpUserResp.getCityName());
        noStatusResp.setDeptCode(ehrEmpUserResp.getDeptCode());
        noStatusResp.setDeptName(ehrEmpUserResp.getDeptName());
        noStatusResp.setTreePath(ehrEmpUserResp.getOrgTree());
        noStatusResp.setTreePathName(ehrEmpUserResp.getOrgDesc());
        return noStatusResp;
    }
}
