package com.ziroom.tech.demeterapi.config;

import com.google.common.collect.Lists;
import com.ziroom.tech.demeterapi.common.OperatorContext;
import com.ziroom.tech.demeterapi.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author daijiankun
 */
public class DemeterInterceptor extends HandlerInterceptorAdapter {

    private static final String UID = "uid";
    private static final List<String> swaggerUrl = Lists.newArrayList("/doc.html", "/swagger-resources");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String servletPath = request.getServletPath();

        if (RequestMethod.OPTIONS.name().equals(request.getMethod())) {
            return true;
        }
        if (swaggerUrl.contains(servletPath)) {
            return true;
        }
        if (servletPath.startsWith("/webjars")) {
            return true;
        }
//        String uid = request.getHeader(UID);
//        if (StringUtils.isNotBlank(uid)) {
//            OperatorContext.setOperator(uid);
//            return true;
//        } else {
//            throw new BusinessException("uid不存在");
//        }
        OperatorContext.setOperator();
        return true;
    }
}
