package com.ziroom.tech.demeterapi.open.login.controller;

import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.common.utils.ModelResultUtil;
import com.ziroom.tech.demeterapi.open.facade.CookieFacade;
import com.ziroom.tech.demeterapi.open.facade.LocalFacade;
import com.ziroom.tech.demeterapi.open.facade.RedisFacade;
import com.ziroom.tech.demeterapi.open.login.model.OperatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author: xuzeyu
 */
@Slf4j
@RestController
@RequestMapping("open/api/logOut")
@ConditionalOnExpression("'${spring.profiles.active}'.equals('test')")
public class TestOpenLogoutController {

    /**
     * 登出
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelResult<Boolean> logout(HttpSession session, HttpServletResponse response) {
        //获取当前登录人
        String operator = OperatorContext.getOperator();
        // 删除记录 Redis
        LocalFacade.delete(operator);
        // 销毁 session
        session.invalidate();
        // 记录登出日志
        log.info("[OpenLogoutController] 用户退出登录:{}", operator);
        return ModelResultUtil.success(true);
    }

}
