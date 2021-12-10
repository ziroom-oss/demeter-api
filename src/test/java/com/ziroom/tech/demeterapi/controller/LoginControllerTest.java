package com.ziroom.tech.demeterapi.controller;

import com.alibaba.fastjson.JSON;
import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.login.model.JwtSubjectModel;
import com.ziroom.tech.demeterapi.open.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author libingsi
 * @date 2021/12/10 15:11
 */
@Slf4j
public class LoginControllerTest {

    /**
     * 生成jwt
     * @throws Exception
     */
    @Test
    public void createDemeterJWT() throws Exception{
        String sopWebJWT = JwtUtils.createDemeterJWT(JSON.toJSONString(new JwtSubjectModel("10033587", System.currentTimeMillis())));
        log.info("jwt:{}",sopWebJWT);
        Assert.assertNotNull(sopWebJWT);
    }

    /**
     * 解析jwt
     * @throws Exception
     */
    @Test
    public void parseJWT() throws Exception{
        String jwtStr = JwtUtils.createDemeterJWT(JSON.toJSONString(new JwtSubjectModel("10033587", System.currentTimeMillis())));
        ModelResult<JwtSubjectModel> modelResult = JwtUtils.parseJWT(jwtStr);
        log.info("modelResult:{}",JSON.toJSON(modelResult));
        Assert.assertNotNull(modelResult);
        Assert.assertTrue("true", modelResult.getSuccess());
    }
}
