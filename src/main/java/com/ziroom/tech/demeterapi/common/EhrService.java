package com.ziroom.tech.demeterapi.common;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ziroom.tech.demeterapi.common.exception.BusinessException;
import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.ehr.client.service.EhrServiceClient;
import com.ziroom.tech.demeterapi.po.dto.req.ehr.EhrEmpListReq;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 旧版用户服务 和 OpenEhrService 同属用户服务 后期会将此部分转移至 OpenEhrService
 * @author xuzeyu
 **/
@Slf4j
@Component
public class EhrService {

    @Autowired
    private EhrServiceClient ehrServiceClient;

    /**
     * 根据姓名模糊查询用户
     */
    public List<UserResp> getEmpList(EhrEmpListReq ehrEmpListReq) {
        String name = ehrEmpListReq.getEmpCodeNameAdcode();
        ModelResult<List<UserDetailResp>> modelResult = ehrServiceClient.getAllUsers(name);
        if(!modelResult.isSuccess()){
            log.error("[EhrService] ehrServiceClient.getAllUsers exception", JSON.toJSONString(modelResult));
            //throw new BusinessException("查询用户信息失败");
            return new ArrayList<>();
        }
        List<UserDetailResp> userDetailRespList = modelResult.getResult();
        List<UserResp> resp = Lists.newArrayList();
        for(UserDetailResp userDetailResp : userDetailRespList){
            UserResp ehrUserResp = new UserResp();
            ehrUserResp.setName(userDetailResp.getUserName());
            ehrUserResp.setCode(userDetailResp.getUserCode());
            ehrUserResp.setEmail(userDetailResp.getEmail());
            resp.add(ehrUserResp);
        }
        return resp;
    }

    /**
     * 查询用户详情
     */
    public UserDetailResp getUserDetail(String userCode) {
        ModelResult<UserDetailResp> userInfoModelResult = ehrServiceClient.getUserInfo(userCode);
        if(!userInfoModelResult.isSuccess()){
            log.error("[EhrService] ehrServiceClient.getUserInfo exception", JSON.toJSONString(userInfoModelResult));
            throw new BusinessException("查询用户信息失败");
        }
        return userInfoModelResult.getResult();
    }

    /**
     * 查询部门根信息
     */
    public EhrDeptResp getRootOrgList() {
        ModelResult<EhrDeptResp> rootDeptModelResult = ehrServiceClient.getRootDept();
        if(!rootDeptModelResult.isSuccess()){
            log.error("[EhrService] ehrServiceClient.getRootDept exception", JSON.toJSONString(rootDeptModelResult));
            throw new BusinessException("查询部门根信息失败");
        }

        return rootDeptModelResult.getResult();
    }

    /**
     * 查询部门下人员信息
     */
    public List<UserResp> getEmpListByDept(EhrEmpListReq ehrEmpListReq){
        String orgCode = ehrEmpListReq.getOrgCode();
        ModelResult<List<UserDetailResp>> empListByDeptModelResult = ehrServiceClient.getEmpListByDept(orgCode);
        if(!empListByDeptModelResult.isSuccess()){
            log.error("[EhrService] ehrServiceClient.getEmpListByDept exception", JSON.toJSONString(empListByDeptModelResult));
            return new ArrayList<>();
        }
        List<UserResp> resp = Lists.newArrayList();
        for(UserDetailResp model : empListByDeptModelResult.getResult()){
            UserResp userResp = new UserResp();
            userResp.setName(model.getUserName());
            userResp.setCode(model.getUserCode());
            userResp.setEmail(model.getEmail());
            resp.add(userResp);
        }
        return resp;
    }

    /**
     * 通过父部门id获取子部门
     */
    public List<EhrDeptResp> getChildDept(String depeCode) {
        ModelResult<List<EhrDeptResp>> childDeptModelResult = ehrServiceClient.getChildDept(depeCode);
        if(!childDeptModelResult.isSuccess()){
            log.error("[EhrService] ehrServiceClient.getChildDept exception", JSON.toJSONString(childDeptModelResult));
            throw new BusinessException("查询部门信息失败");
        }
        return childDeptModelResult.getResult();
    }

    /**
     * 通过部门编码查询部门信息
     */
    public EhrDeptResp getOrgByCode(String deptCode) {
        ModelResult<EhrDeptResp> deptInfoModelResult = ehrServiceClient.getDeptInfo(deptCode);
        if(!deptInfoModelResult.isSuccess()){
            log.error("[EhrService] ehrServiceClient.getDeptInfo exception", JSON.toJSONString(deptInfoModelResult));
            throw new BusinessException("查询部门信息失败");
        }
        return deptInfoModelResult.getResult();
    }

}
