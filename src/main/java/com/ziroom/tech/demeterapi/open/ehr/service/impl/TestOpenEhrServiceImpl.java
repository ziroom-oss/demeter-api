package com.ziroom.tech.demeterapi.open.ehr.service.impl;

import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.ehr.client.service.EhrServiceClient;
import com.ziroom.tech.demeterapi.open.ehr.dto.EhrUserInfoDto;
import com.ziroom.tech.demeterapi.open.ehr.service.OpenEhrService;
import com.ziroom.tech.demeterapi.open.common.utils.ModelResultUtil;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
