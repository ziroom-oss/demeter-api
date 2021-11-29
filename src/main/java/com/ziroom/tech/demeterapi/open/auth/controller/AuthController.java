package com.ziroom.tech.demeterapi.open.auth.controller;

import com.ziroom.tech.demeterapi.open.common.model.ModelResponse;
import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.common.utils.ModelResponseUtil;
import com.ziroom.tech.demeterapi.open.auth.model.AuthModelResp;
import com.ziroom.tech.demeterapi.open.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xuzeyu
 */
@RestController
@Slf4j
@RequestMapping(value = "open/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("user/role")
    public ModelResponse<AuthModelResp> getCurrentAuth() {
        ModelResult<AuthModelResp> modelResult = authService.getAuth();
        if(!modelResult.isSuccess()){
            return ModelResponseUtil.error("-1", modelResult.getResultMessage());
        }
        AuthModelResp authModelResp = modelResult.getResult();
        return ModelResponseUtil.ok(authModelResp);
    }

}
