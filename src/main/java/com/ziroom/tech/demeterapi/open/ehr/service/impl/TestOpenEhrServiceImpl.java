package com.ziroom.tech.demeterapi.open.ehr.service.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.ehr.client.service.EhrServiceClient;
import com.ziroom.tech.demeterapi.open.ehr.dto.EhrDepartmentInfoDto;
import com.ziroom.tech.demeterapi.open.ehr.dto.EhrUserInfoDto;
import com.ziroom.tech.demeterapi.open.ehr.service.OpenEhrService;
import com.ziroom.tech.demeterapi.open.common.utils.ModelResultUtil;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * 用户服务
 * @author xuzeyu
 */
@Service("testOpenEhrService")
public class TestOpenEhrServiceImpl implements OpenEhrService {

    @Autowired
    private EhrServiceClient ehrServiceClient;

    /**
     * 个人基本信息展现
     */
    public ModelResult<EhrUserInfoDto> getUserInfo(String uid) {
        ModelResult<UserDetailResp> userInfoModelResult = ehrServiceClient.getUserInfo(uid);
        if(!userInfoModelResult.isSuccess()){
            EhrUserInfoDto ehrUserInfoDto = new EhrUserInfoDto();
            ehrUserInfoDto.setUsername("马可波罗");
            ehrUserInfoDto.setEmail("xxx@ziroom.com");
            ehrUserInfoDto.setEducation("硕士");
            ehrUserInfoDto.setHireDays(100);
            ehrUserInfoDto.setJob("java开发工程师");
            ehrUserInfoDto.setPosition("T3");
            ehrUserInfoDto.setSkills("线程池,zookeeper");
            return ModelResultUtil.success(ehrUserInfoDto);
        }
        UserDetailResp userDetailResp = userInfoModelResult.getResult();
        EhrUserInfoDto ehrUserInfoDto = new EhrUserInfoDto();
        ehrUserInfoDto.setUsername(userDetailResp.getUserName());
        ehrUserInfoDto.setEmail(userDetailResp.getEmail());
        ehrUserInfoDto.setEducation("本科");
        ehrUserInfoDto.setHireDays(100);
        ehrUserInfoDto.setJob("java开发工程师");
        ehrUserInfoDto.setPosition("T3");
        ehrUserInfoDto.setSkills("线程池, zookeeper");
        return ModelResultUtil.success(ehrUserInfoDto);
    }

    /**
     * 部门列表
     */
    public ModelResult<List<EhrDepartmentInfoDto>> getDepartmentList() {
        List<EhrDepartmentInfoDto> departmentInfoDtos = ImmutableList.of(new EhrDepartmentInfoDto("102558","互联网产品技术平台"),
                new EhrDepartmentInfoDto("102303","互联网营销运营平台"),
                new EhrDepartmentInfoDto("100117","智能家装事业群"),
                new EhrDepartmentInfoDto("100363","自如家服平台"));
        return ModelResultUtil.success(departmentInfoDtos);
    }

    /**
     * 根据用户系统号查询用户名称
     */
    public ModelResult<List<UserResp>> getUserDetail(List<String> userCodes) {
        List<UserResp> userRespList = Lists.newArrayList();
        for(String userCode: userCodes){
            UserResp userResp = new UserResp();
            userResp.setCode(userCode);
            userResp.setName("徐泽宇");
            userResp.setEmail("xuzy5@ziroom.com");
            userRespList.add(userResp);
        }

        return ModelResultUtil.success(userRespList);
    }

}
