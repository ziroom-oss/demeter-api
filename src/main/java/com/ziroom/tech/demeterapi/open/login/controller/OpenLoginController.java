package com.ziroom.tech.demeterapi.open.login.controller;

import com.alibaba.fastjson.JSON;
import com.ziroom.tech.demeterapi.open.common.constant.SystemConstants;
import com.ziroom.tech.demeterapi.open.common.enums.ResponseEnum;
import com.ziroom.tech.demeterapi.open.common.model.ModelResponse;
import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.common.utils.ModelResponseUtil;
import com.ziroom.tech.demeterapi.open.common.utils.ModelResultUtil;
import com.ziroom.tech.demeterapi.open.ehr.service.OpenEhrService;
import com.ziroom.tech.demeterapi.open.facade.CookieFacade;
import com.ziroom.tech.demeterapi.open.facade.RedisFacade;
import com.ziroom.tech.demeterapi.open.login.dto.UserInfoDto;
import com.ziroom.tech.demeterapi.open.login.model.JwtSubjectModel;
import com.ziroom.tech.demeterapi.open.login.param.LogInUserParam;
import com.ziroom.tech.demeterapi.open.login.vo.LoginResultVo;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.tech.demeterapi.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 登录
 * @author xuzeyu
 */
@RestController
@Slf4j
@RequestMapping("open/api/login")
@ConditionalOnExpression("!'${spring.profiles.active}'.equals('test')")
public class OpenLoginController {

    @Resource(name = "testOpenEhrService")
    private OpenEhrService openEhrService;

    @Autowired
    private CookieFacade cookieFacade;

    @Autowired
    private RedisFacade redisFacade;

    /**
     * 用户名密码登录
     */
    @RequestMapping(value = "/usernamePassword", method = RequestMethod.POST)
    public ModelResponse<LoginResultVo> usernamePassword(@Validated @RequestBody LogInUserParam loginParam,
                                                       HttpSession session, HttpServletResponse response) {
        // 统一登录模块
        ModelResult<LoginResultVo> loginResultVoWebModelResult = commonLoginHandle(loginParam);
        return ModelResponseUtil.ok(loginResultVoWebModelResult.getResult());
    }

    /**
     * 登录态校验
     */
    @GetMapping("/check")
    @ResponseBody
    public ModelResponse<Map<String, String>> check(HttpServletRequest request) {
        try {
            String jwtToken = request.getHeader(SystemConstants.AUTHORIZATION);
            if (StringUtils.isNotEmpty(jwtToken)) {
                ModelResult<JwtSubjectModel> jwtSubjectModelModelResult = JwtUtils.parseJWT(jwtToken);
                if (jwtSubjectModelModelResult.isSuccess() && Objects.nonNull(jwtSubjectModelModelResult.getResult())) {
                    String userCode = jwtSubjectModelModelResult.getResult().getCode();
                    ModelResult<UserInfoDto> userInfoModelResult = openEhrService.getUserInfo(userCode);
                    UserInfoDto userInfoDto = userInfoModelResult.getResult();
                    return ModelResponseUtil.ok(new HashMap(2) {{
                        this.put("userCode", userInfoDto.getUserCode());
                        this.put("username", userInfoDto.getUserName());
                        this.put("token", jwtToken);
                    }});
                }
            }
        } catch (Exception e) {
            return ModelResponseUtil.error(ResponseEnum.ERROR_CODE.getCode(), "登录校验异常");
        }
        return ModelResponseUtil.error(ResponseEnum.FRONT_NO_LOGIN.getCode(), ResponseEnum.FRONT_NO_LOGIN.getMessage());
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

            // 存Redis
            userDetailResp.setLoginCode(loginParam.getLoginName());
            redisFacade.saveJwt(userDetailResp);

            LoginResultVo loginResultVo = new LoginResultVo(userDetailResp.getUserCode(), sopWebJWT);
            return ModelResultUtil.success(loginResultVo);
        }catch (Exception e){
            log.error("[OpenLoginController] OpenLoginController.commonLoginHandle exception", e);
            return ModelResultUtil.error(ResponseEnum.ERROR_CODE.getCode(),ResponseEnum.ERROR_CODE.getMessage());
        }
    }

}
