package com.ziroom.tech.demeterapi.open.interceptor;

import com.alibaba.fastjson.JSON;
import com.ziroom.tech.demeterapi.open.ehr.client.service.EhrServiceClient;
import com.ziroom.tech.demeterapi.open.login.model.OperatorContext;
import com.ziroom.tech.demeterapi.open.facade.RedisFacade;
import com.ziroom.tech.demeterapi.open.common.constant.ContentTypeEnum;
import com.ziroom.tech.demeterapi.open.common.constant.RedisConstants;
import com.ziroom.tech.demeterapi.open.common.constant.SystemConstants;
import com.ziroom.tech.demeterapi.open.common.enums.ResponseEnum;
import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.common.model.SopUserRedisStoreModel;
import com.ziroom.tech.demeterapi.open.login.model.JwtSubjectModel;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.tech.demeterapi.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 登录拦截器
 * @author: xuzeyu
 */
@Slf4j
@Component("loginHandlerInterceptor")
@ConditionalOnExpression("!'${spring.profiles.active}'.equals('test')")
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisFacade redisFacade;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith(request.getContextPath() + SystemConstants.NO_INTERCEPT_URI)
                || requestURI.equals(request.getContextPath() + "/error")
                || requestURI.equals(request.getContextPath() + "/")
                || requestURI.equals(request.getContextPath() + "")
        ) {
            return true;
        }
        String jwtToken = request.getHeader(SystemConstants.AUTHORIZATION);
        if (jwtToken == null || StringUtils.isBlank(jwtToken.trim())) {
            String contentType = request.getContentType();
            if (contentType != null && contentType.startsWith(ContentTypeEnum.TEXT_HTML.value)) {
                //返回 无权限页
                response.sendRedirect(request.getContextPath() + "/403.html");
            } else {
                // 返回 JSON
                Map<String, Object> stringObjectMap = resultMessage(ResponseEnum.PERMISSION_DENIED, null);
                responseOutWithJson(response, stringObjectMap);
            }
            return false;
        }
        ModelResult<JwtSubjectModel> jwtSubjectModelModelResult = JwtUtils.parseJWT(jwtToken);
        if (!jwtSubjectModelModelResult.isSuccess() || jwtSubjectModelModelResult.getResult() == null) {
            responseOutWithJson(response, jwtSubjectModelModelResult);
            return false;
        }
        JwtSubjectModel jwtSubjectModel = jwtSubjectModelModelResult.getResult();
        log.info("[LoginInterceptor] 当前登录用户: {}", JSON.toJSONString(jwtSubjectModel));

        // 续签 -- 如果每次访问都续签，大大浪费Redis的读写， 目前 是 15钟后才续签，Redis 存储 45分钟，极限是半小时登录失效，最大是45分钟失效
        //
        //       |~~~~|______________:______________.____________|
        //      token |             续签                         |
        //      生成  存            时间                        过期
        //      时间  Redis                                     时间
        //            时间
        String userCode = jwtSubjectModel.getCode();
        SopUserRedisStoreModel currentUser = redisFacade.getCurrentUser(userCode);
        if (currentUser == null) {
            Map<String, Object> stringObjectMap = resultMessage(ResponseEnum.FRONT_LOGIN_USER_TOKEN_EXPIRE, null);
            responseOutWithJson(response, stringObjectMap);
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        log.info("[LoginInterceptor] 登录用户 : {} , token的创建时间：{} ,用户续签次数:{}, 用户续签时间:{}",
                userCode, Instant.ofEpochMilli(jwtSubjectModel.getCts()), currentUser.getRenewTimes(), Instant.ofEpochMilli(currentUser.getRenewTimeStamp()));

        long min = currentUser.getRenewTimeStamp() + RedisConstants.DEMETER_FRONT_LOGIN_RENEW_EXPIRE * 1000;
        if (currentTimeMillis > min) {
            // 续签
            redisFacade.renew(currentUser);
        }

        //存储登录用户信息
        UserDetailResp userDetailResp = new UserDetailResp();
        userDetailResp.setLoginCode(currentUser.getLoginCode());
        userDetailResp.setUserCode(currentUser.getUserCode());
        userDetailResp.setUserName(currentUser.getUserName());
        userDetailResp.setDeptCode(currentUser.getDeptCode());
        OperatorContext.setOperator(userDetailResp);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        OperatorContext.remove();
    }


    /**
     * 写出数据
     */
    private void responseOutWithJson(HttpServletResponse response,
                                       Object responseObject) {
        String result = JSON.toJSONString(responseObject);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try(PrintWriter out = response.getWriter()) {
            out.write(result);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> resultMessage(ResponseEnum returnCodeEnum, String message) {
        Map<String, Object> resultMessage = new LinkedHashMap<>(8);
        resultMessage.put("success", returnCodeEnum == ResponseEnum.RESPONSE_SUCCESS_CODE ? true : false);
        resultMessage.put("code", returnCodeEnum.getCode());
        resultMessage.put("errorMessage", message == null ? returnCodeEnum.getMessage() : message);
        return resultMessage;
    }
}
