package com.ziroom.tech.demeterapi.po.qo;

import io.swagger.annotations.ApiModel;
import lombok.Data;


@Data
@ApiModel("排行榜部门Po")
public class UserJoinDept {

    private String receiverUid;

    private String empName;

    private String deptCode;

    private String deptName;

    private Long sumAll;
    //private List<DemeterTaskUser> dtus;

}
