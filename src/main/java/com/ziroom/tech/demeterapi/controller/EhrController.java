package com.ziroom.tech.demeterapi.controller;

import com.alibaba.fastjson.JSON;
import com.ziroom.tech.demeterapi.common.EhrComponent;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.ehr.*;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.EhrDeptResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author huangqiaowei
 * @date 2020-06-16 18:37
 **/
@RestController
@RequestMapping("api/ehr")
@Slf4j
@Api(tags = "查询ehr接口")
public class EhrController {

    @Resource
    private EhrComponent ehrComponent;

    @PostMapping("getEmpList")
    @ApiModelProperty("模糊查询用户")
    public Resp<List<UserResp>> getEmpList(@RequestBody EhrEmpListReq ehrEmpListReq) {
        log.info("EhrController.getEmpList params:{}", JSON.toJSONString(ehrEmpListReq));
        return Resp.success(ehrComponent.getEmpList(ehrEmpListReq));
    }
    @PostMapping("getEmp")
    @ApiModelProperty("查询用户")
    public Resp<List<UserResp>> getEmp(@RequestBody EhrEmpListReq ehrEmpListReq) {
        log.info("EhrController.getEmp params:{}", JSON.toJSONString(ehrEmpListReq));
        return Resp.success(ehrComponent.getEmpList(ehrEmpListReq));
    }
    @PostMapping("nextOrg")
    @ApiOperation(value = "根据部门编码，查询子部门列表", notes = "目前公司编码默认为101", httpMethod = "POST")
    public Resp<Set<EhrDeptResp>> getNextOrgEmpList(@RequestBody EhrNextOrgReq ehrNextOrgReq) {
        ehrNextOrgReq.validate();
        log.info("EhrController.getNextOrg params:{}", JSON.toJSONString(ehrNextOrgReq));
        ehrNextOrgReq.setSetId("101");
        return Resp.success(ehrComponent.getChildOrgs(ehrNextOrgReq.getDeptId(), ehrNextOrgReq.getSetId()));
    }

    @PostMapping("getOrgList")
    @ApiOperation(value = "根据查询条件获取部门列表", notes = "可用于查询部门树的根使用", httpMethod = "POST")
    public Resp<Set<EhrDeptResp>> getOrgList(@RequestBody EhrOrgListReq ehrOrgListReq) {
        log.info("EhrController.getOrgList params:{}", JSON.toJSONString(ehrOrgListReq));
        return Resp.success(ehrComponent.getOrgList(ehrOrgListReq));
    }

    @PostMapping("getOrgByCode")
    @ApiOperation(value = "根据部门编码获取部门信息", httpMethod = "POST")
    public Resp<EhrDeptResp> getOrgByCode(@RequestBody EhrOrgReq ehrOrgReq) {
        ehrOrgReq.validate();
        log.info("EhrController.getOrgByCode params:{}", JSON.toJSONString(ehrOrgReq));
        ehrOrgReq.setSetId("101");
        return Resp.success(ehrComponent.getOrgByCode(ehrOrgReq.getDeptId(), ehrOrgReq.getSetId()));
    }

    @PostMapping("queryDetail")
    @ApiOperation("查询用户详情")
    public Resp<UserDetailResp> queryDetail(@RequestBody UserDetailReq userDetailReq){
        userDetailReq.validate();
        log.info("EhrController.queryDetail params:{}", JSON.toJSONString(userDetailReq));
        return Resp.success(ehrComponent.getUserDetail(userDetailReq.getUserCode()));
    }

    @PostMapping("thirdDept")
    @ApiOperation(value = "获取所有的三级部门", httpMethod = "POST")
    public Resp<Set<EhrDeptResp>> getAllThirdDepartment() {
        log.info("EhrController.getAllThirdDepartment");
        return Resp.success(ehrComponent.getAllThirdOrgList());
    }

    @PostMapping("currentDept")
    @ApiOperation(value = "获取当前登录人的三级部门")
    public Resp<EhrDeptResp> getCurrentDept() {
        log.info("EhrController.getCurrentDept");
        return Resp.success(ehrComponent.getCurrentThirdDept());
    }

    @PostMapping("principalDeptTree")
    @ApiOperation(value = "获取当前登录人的主职部门树")
    public Resp<String> getPrincipalDeptTree() {
        log.info("EhrController.getCurrentDept");
        return Resp.success(ehrComponent.getPrincipalDept());
    }
}
