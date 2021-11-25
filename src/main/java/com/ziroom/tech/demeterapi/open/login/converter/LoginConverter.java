package com.ziroom.tech.demeterapi.open.login.converter;

import com.ziroom.tech.demeterapi.open.login.dto.UserInfoDto;
import com.ziroom.tech.demeterapi.open.login.vo.UserInfoVO;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;

import java.util.function.Function;

/**
 * login model 转换器
 * @author xuzeyu
 */
public class LoginConverter {

    /**
     * 用户信息model转换
     * @return
     */
    public static Function<UserDetailResp, UserInfoDto> EhrUserInfoConverter() {
        return userDetailResp -> {
            UserInfoDto userInfoDto = new UserInfoDto();
            userInfoDto.setUserCode(userDetailResp.getUserCode());
            userInfoDto.setUserName(userDetailResp.getUserName());
            userInfoDto.setJob(userDetailResp.getJob());
            userInfoDto.setJobId(userDetailResp.getJobId());
            userInfoDto.setDept(userDetailResp.getDept());
            userInfoDto.setDeptCode(userDetailResp.getDeptCode());
            return userInfoDto;
        };
    }

    /**
     * 用户信息model转换
     * @return
     */
    public static Function<UserInfoDto, UserInfoVO> EhrUserDtoToVOConverter() {
        return userInfoDto -> {
            UserInfoVO userInfoVO = new UserInfoVO();
            userInfoVO.setUserCode(userInfoDto.getUserCode());
            userInfoVO.setUserName(userInfoDto.getUserName());
            userInfoVO.setJob(userInfoDto.getJob());
            userInfoVO.setJobId(userInfoDto.getJobId());
            userInfoVO.setDept(userInfoDto.getDept());
            userInfoVO.setDeptCode(userInfoDto.getDeptCode());
            return userInfoVO;
        };
    }

}
