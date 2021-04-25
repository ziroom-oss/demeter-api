package com.ziroom.tech.demeterapi.config;

import com.google.common.collect.Lists;
import com.ziroom.tech.demeterapi.common.OperatorContext;
import com.ziroom.tech.demeterapi.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 拦截器
 *
 * @author huangqiaowei
 * @date 2019-10-05 23:58
 **/
@Slf4j
public class NavigationInterceptor extends HandlerInterceptorAdapter {

    private static final String UID = "uid";
    private static final String WEB_JARS = "/webjars";
    private static final List<String> SWAGGER_URL = Lists.newArrayList("/doc.html","/swagger-resources");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String servletPath = request.getServletPath();
        log.info("request path：{}", servletPath);
        if (SWAGGER_URL.contains(servletPath)) {
            return true;
        }
        if (servletPath.startsWith(WEB_JARS)){
            return true;
        }
        String uid = request.getHeader(UID);
        if (StringUtils.isNotBlank(uid)) {
            OperatorContext.setOperator(uid);
            return true;
        } else {
            if(CorsUtils.isCorsRequest(request)){
                return true;
            }
            throw new BusinessException("获取用户uid失败");
        }
    }
}
