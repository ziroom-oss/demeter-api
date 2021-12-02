package com.ziroom.tech.demeterapi.test;

import com.alibaba.fastjson.JSON;
import com.ziroom.tech.demeterapi.open.common.model.ModelResponse;
import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.common.utils.ModelResponseUtil;
import com.ziroom.tech.demeterapi.open.login.model.JwtSubjectModel;
import com.ziroom.tech.demeterapi.open.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuzeyu
 */
@RestController
@Slf4j
@RequestMapping("test/api/login")
public class TestLoginController {

    /**
     * 生成jwt
     */
    @RequestMapping(value = "/createJWT", method = RequestMethod.GET)
    public ModelResponse<String> createJWT() {
        try {
            String sopWebJWT = JwtUtils.createDemeterJWT(JSON.toJSONString(new JwtSubjectModel("10033587", System.currentTimeMillis())));
            return ModelResponseUtil.ok(sopWebJWT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ModelResponseUtil.error("create fail");
    }

    /**
     * 解析jwt
     */
    @RequestMapping(value = "/praseJWT", method = RequestMethod.GET)
    public ModelResponse<JwtSubjectModel> praseJWT(String jwtStr) {
        try {
            ModelResult<JwtSubjectModel> modelResult = JwtUtils.parseJWT(jwtStr);
            return ModelResponseUtil.ok(modelResult.getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ModelResponseUtil.error("prase fail");
    }


}
