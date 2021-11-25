package com.ziroom.tech.demeterapi.interceptor;

import com.google.common.collect.Lists;
import com.ziroom.tech.demeterapi.common.OperatorContext;
import com.ziroom.tech.demeterapi.common.UserParamThreadLocal;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author daijiankun
 */
public class UserLoginInterceptor implements HandlerInterceptor {

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

        //获取请求中的token 根据token拿到用户信息
        //将用户id存储到ThreadLocal
        OperatorContext.setOperator();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserParamThreadLocal.remove();
    }
}
