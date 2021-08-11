package com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.resp;

import com.alibaba.fastjson.JSONObject;
import com.ziroom.tech.demeterapi.common.enums.CityCodeEnum;
import java.util.Objects;
import java.util.Optional;
import lombok.Data;

/**
 * ehr-api emp/list 接口
 *
 * @author huangqiaowei
 * @date 2020-08-24 11:18
 **/
@Data
public class EhrEmpUserResp {

    private String empCode;
    private String empName;
    private String email;
    private String phone;
    private String jobCode;
    private String jobName;
    private String deptCode;
    private String deptName;
    private String orgTree;
    private String orgDesc;
    private String cityCode;
    private String cityName;

    public static EhrEmpUserResp copyByJson(JSONObject jsonObject) {
        EhrEmpUserResp ehrEmpUserResp = new EhrEmpUserResp();
        ehrEmpUserResp.setEmpCode(jsonObject.getString("empCode"));
        ehrEmpUserResp.setEmpName(jsonObject.getString("empName"));
        ehrEmpUserResp.setEmail(jsonObject.getString("email"));
        ehrEmpUserResp.setPhone(jsonObject.getString("phone"));
        jsonObject.getJSONArray("jobs").stream().map(o -> (JSONObject) o).forEach(o -> {
            if (Objects.equals(o.getString("jobIndicator"), "P")) {
                ehrEmpUserResp.setJobCode(o.getString("jobCode"));
                ehrEmpUserResp.setJobName(o.getString("jobName"));
                ehrEmpUserResp.setDeptCode(o.getString("deptCode"));
                ehrEmpUserResp.setDeptName(o.getString("deptName"));
                ehrEmpUserResp.setOrgTree(o.getString("orgTree"));
                ehrEmpUserResp.setOrgDesc(o.getString("orgDesc"));
                ehrEmpUserResp.setCityCode(o.getString("cityCode"));
                ehrEmpUserResp.setCityName(Optional.ofNullable(CityCodeEnum.getByCode(ehrEmpUserResp.getCityCode())).map(CityCodeEnum::getName).orElse(null));
            }
        });
        return ehrEmpUserResp;
    }
}
