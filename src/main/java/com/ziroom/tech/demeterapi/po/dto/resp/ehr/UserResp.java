package com.ziroom.tech.demeterapi.po.dto.resp.ehr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huangqiaowei
 * @date 2020-06-08 14:30
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResp {

    private String code;

    private String name;

    private String email;
}
