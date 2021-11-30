package com.ziroom.tech.demeterapi.open.interceptor;

import com.alibaba.fastjson.JSON;
import com.ziroom.tech.demeterapi.common.OperatorContext;
import com.ziroom.tech.demeterapi.open.common.constant.ContentTypeEnum;
import com.ziroom.tech.demeterapi.open.common.constant.SystemConstants;
import com.ziroom.tech.demeterapi.open.common.enums.ResponseEnum;
import com.ziroom.tech.demeterapi.open.facade.LocalFacade;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 测试时生效的 登录拦截器
 * @author: xuzeyu
 */
@Slf4j
@Component("handlerInterceptor")
@ConditionalOnExpression("'${spring.profiles.active}'.equals('test')")
public class TestLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith(request.getContextPath() + SystemConstants.NO_INTERCEPT_URI)
                || requestURI.equals(request.getContextPath() + "/error")
                || requestURI.equals(request.getContextPath() + "/ok")) {
            return true;
        }
        String jwtToken = request.getHeader(SystemConstants.AUTHORIZATION);
        if (jwtToken == null || StringUtils.isBlank(jwtToken)) {
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
        UserDetailResp loginInfo = LocalFacade.getLoginInfo(jwtToken);
        if (Objects.isNull(loginInfo)) {
            Map<String, Object> stringObjectMap = resultMessage(ResponseEnum.FRONT_LOGIN_USER_TOKEN_EXPIRE, null);
            responseOutWithJson(response, stringObjectMap);
            return false;
        }

        //存储登录用户code
        OperatorContext.setOperator();
        return true;
    }

    /**
     * 写出数据
     */
    protected void responseOutWithJson(HttpServletResponse response,
                                       Object responseObject) {
        String result = JSON.toJSONString(responseObject);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(result);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    protected Map<String, Object> resultMessage(ResponseEnum returnCodeEnum, String message) {
        Map<String, Object> resultMessage = new LinkedHashMap<>(8);
        resultMessage.put("success", returnCodeEnum == ResponseEnum.RESPONSE_SUCCESS_CODE ? true : false);
        resultMessage.put("code", returnCodeEnum.getCode());
        resultMessage.put("errorMessage", message == null ? returnCodeEnum.getMessage() : message);
        return resultMessage;
    }
}
