package com.ziroom.tech.demeterapi.interceptor;

import com.ziroom.tech.demeterapi.common.OperatorContext;
import com.ziroom.tech.demeterapi.common.UserParam;
import com.ziroom.tech.demeterapi.common.UserParamThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xuzeyu
 */
public class UserParamInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String operator = OperatorContext.getOperator();
        UserParam userParam = new UserParam();
        userParam.setUserId(operator);
        //userParam.setUserName("代建坤");
        userParam.setDeptCode("102558");
        UserParamThreadLocal.set(userParam);
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
