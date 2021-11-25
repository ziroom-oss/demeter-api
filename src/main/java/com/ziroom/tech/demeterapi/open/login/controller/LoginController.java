package com.ziroom.tech.demeterapi.open.login.controller;

import com.alibaba.fastjson.JSON;
import com.ziroom.tech.demeterapi.common.OperatorContext;
import com.ziroom.tech.demeterapi.open.common.model.ModelResponse;
import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.common.utils.ModelResponseUtil;
import com.ziroom.tech.demeterapi.open.common.utils.ModelResultUtil;
import com.ziroom.tech.demeterapi.open.login.converter.LoginConverter;
import com.ziroom.tech.demeterapi.open.login.dto.UserInfoDto;
import com.ziroom.tech.demeterapi.open.login.service.LoginService;
import com.ziroom.tech.demeterapi.open.login.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * @author xuzeyu
 */
@RestController
@Slf4j
@RequestMapping("open/api/login")
public class LoginController {

    @Resource(name = "testLoginService")
    private LoginService loginService;

    /**
     * 个人基本信息展现
     */
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public ModelResponse<UserInfoVO> getUserInfo() {
        //获取当前登录人
        String operator = OperatorContext.getOperator();
        //获取用户信息
        ModelResult<UserInfoDto> modelResult = loginService.getUserInfo(operator);
        if(!ModelResultUtil.isSuccess(modelResult)){
            log.warn("[LoginController] loginService.getUserInfo result is {}", JSON.toJSONString(modelResult));
            return ModelResponseUtil.error(modelResult.getResultCode(), modelResult.getResultMessage());
        }
        UserInfoDto userInfoDto = modelResult.getResult();
        UserInfoVO userInfoVO = LoginConverter.EhrUserDtoToVOConverter().apply(userInfoDto);
        return ModelResponseUtil.ok(userInfoVO);
    }


}
