package com.ziroom.tech.demeterapi.open.login.controller;

import com.alibaba.fastjson.JSON;
import com.ziroom.tech.demeterapi.open.common.enums.ResponseEnum;
import com.ziroom.tech.demeterapi.open.common.model.ModelResponse;
import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.common.utils.ModelResponseUtil;
import com.ziroom.tech.demeterapi.open.common.utils.ModelResultUtil;
import com.ziroom.tech.demeterapi.open.ehr.service.OpenEhrService;
import com.ziroom.tech.demeterapi.open.facade.CookieFacade;
import com.ziroom.tech.demeterapi.open.facade.LocalFacade;
import com.ziroom.tech.demeterapi.open.login.model.JwtSubjectModel;
import com.ziroom.tech.demeterapi.open.login.param.LogInUserParam;
import com.ziroom.tech.demeterapi.open.login.vo.LoginResultVo;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.tech.demeterapi.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录
 * @author xuzeyu
 */
@RestController
@Slf4j
@RequestMapping("test/open/api/login")
public class TestOpenLoginController {

    @Resource(name = "testOpenEhrService")
    private OpenEhrService openEhrService;

    @Autowired
    private CookieFacade cookieFacade;

    /**
     * 用户名密码登录
     */
    @RequestMapping(value = "/usernamePassword", method = RequestMethod.POST)
    public ModelResponse<LoginResultVo> usernamePassword(@Validated @RequestBody LogInUserParam loginParam,
                                                       HttpSession session, HttpServletResponse response) {
        // 统一登录模块
        ModelResult<LoginResultVo> loginResultVoWebModelResult = commonLoginHandle(loginParam);
        // 写 jwt 相关的 cookie
        //cookieFacade.addJWTCookie(response, loginResultVoWebModelResult.getResult());
        return ModelResponseUtil.ok(loginResultVoWebModelResult.getResult());
    }


    /**
     * 通用登录模块
     */
    private ModelResult<LoginResultVo> commonLoginHandle(LogInUserParam loginParam) {
        try{
            // 获取登录用户信息
            ModelResult<UserDetailResp> modelResult = openEhrService.getUserInfoByLogin(loginParam);
            if (!ModelResultUtil.isSuccess(modelResult)) {
                log.error("[LoginController] openEhrService.getUserInfoByLogin result is {}", JSON.toJSONString(modelResult));
                return ModelResultUtil.error(modelResult.getResultCode(), modelResult.getResultMessage());
            }
            // 生成 jwt_token
            UserDetailResp userDetailResp = modelResult.getResult();
            String sopWebJWT = JwtUtils.createDemeterJWT(JSON.toJSONString(new JwtSubjectModel(userDetailResp.getUserCode(), System.currentTimeMillis())));

            // 存本地缓存
            LocalFacade.saveLoginInfo(userDetailResp.getUserCode(), userDetailResp);

            LoginResultVo loginResultVo = new LoginResultVo(sopWebJWT, userDetailResp.getUserCode());
            return ModelResultUtil.success(loginResultVo);
        }catch (Exception e){
            log.error("[OpenLoginController] OpenLoginController.commonLoginHandle exception", e);
            return ModelResultUtil.error(ResponseEnum.ERROR_CODE.getCode(),ResponseEnum.ERROR_CODE.getMessage());
        }
    }

}
