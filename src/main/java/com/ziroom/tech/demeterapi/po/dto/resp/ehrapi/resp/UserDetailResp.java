package com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.resp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户详情
 *
 * @author huangqiaowei
 * @date 2020-07-01 19:41
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Api("用户详情")
public class UserDetailResp {

    private String userCode;

    private String userName;

    private String job;

    private String dept;

    private String deptCode;

    private String group;

    private String phone;

    private String email;

    private String center;
}
