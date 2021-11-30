package com.ziroom.tech.demeterapi.open.facade;

import com.ziroom.tech.demeterapi.open.common.constant.SystemConstants;
import com.ziroom.tech.demeterapi.open.login.vo.LoginResultVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: xuzeyu
 */
@Component
public class CookieFacade {

    /**
     * JWT cookie
     */
    public void addJWTCookie(HttpServletResponse response, LoginResultVo result) {
        if (result != null) {
            String token = result.getToken();
            result.setToken(null);
            if (StringUtils.isNotBlank(token)) {
                Cookie cookie = new Cookie(SystemConstants.JWT_TOKEN, token);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                response.addCookie(cookie);
            }
        }
    }

    /**
     * 删除 cookie
     */
    public void delCookie(String cookieName , HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
