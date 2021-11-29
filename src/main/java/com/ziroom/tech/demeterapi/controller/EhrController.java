package com.ziroom.tech.demeterapi.controller;

import com.alibaba.fastjson.JSON;
import com.ziroom.tech.demeterapi.common.EhrService;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.ehr.*;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.EhrDeptResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author xuzeyu
 **/
@RestController
@RequestMapping("api/ehr")
@Slf4j
public class EhrController {

    @Resource
    private EhrService ehrService;


    /**
     * 模糊查询用户
     */
    @PostMapping("getEmpList")
    public Resp<List<UserResp>> getEmpList(@RequestBody EhrEmpListReq ehrEmpListReq) {
        log.info("EhrController.getEmpList params:{}", JSON.toJSONString(ehrEmpListReq));
        return Resp.success(ehrService.getEmpList(ehrEmpListReq));
    }

    /**
     * 查询用户详情
     */
    @PostMapping("queryDetail")
    public Resp<UserDetailResp> queryDetail(@RequestBody UserDetailReq userDetailReq){
        userDetailReq.validate();
        log.info("EhrController.queryDetail params:{}", JSON.toJSONString(userDetailReq));
        return Resp.success(ehrService.getUserDetail(userDetailReq.getUserCode()));
    }

    /**
     * 查询部门根信息
     */
    @GetMapping("getOrgList")
    public Resp<EhrDeptResp> getRootOrgList() {
        return Resp.success(ehrService.getRootOrgList());
    }

    /**
     * 查询部门下人员信息
     */
    @PostMapping("getEmpListByDept")
    public Resp<List<UserResp>> getEmpListByDept(@RequestBody EhrEmpListReq ehrEmpListReq) {
        log.info("EhrController.getEmpList params:{}", JSON.toJSONString(ehrEmpListReq));
        return Resp.success(ehrService.getEmpListByDept(ehrEmpListReq));
    }

    /**
     * 根据部门编码，查询子部门列表
     */
    @PostMapping("nextOrg")
    public Resp<List<EhrDeptResp>> getChildDept(@RequestBody EhrNextOrgReq ehrNextOrgReq) {
        ehrNextOrgReq.validate();
        log.info("EhrController.getChildDept params:{}", JSON.toJSONString(ehrNextOrgReq));
        return Resp.success(ehrService.getChildDept(ehrNextOrgReq.getDeptId()));
    }

    /**
     * 根据部门编码获取部门信息
     */
    @PostMapping("getOrgByCode")
    public Resp<EhrDeptResp> getOrgByCode(@RequestBody EhrOrgReq ehrOrgReq) {
        ehrOrgReq.validate();
        log.info("EhrController.getOrgByCode params:{}", JSON.toJSONString(ehrOrgReq));
        return Resp.success(ehrService.getOrgByCode(ehrOrgReq.getDeptId()));
    }

    /**
     * 查询用户
     */
    @PostMapping("getEmp")
    public Resp<List<UserResp>> getEmp(@RequestBody EhrEmpListReq ehrEmpListReq) {
        log.info("EhrController.getEmp params:{}", JSON.toJSONString(ehrEmpListReq));
        return Resp.success(ehrService.getEmpList(ehrEmpListReq));
    }

}
