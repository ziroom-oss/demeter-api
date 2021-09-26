package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.service.InitCoreDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author libingsi
 * @date 2021/9/24 下午6:46
 */
@Slf4j
@RestController
@RequestMapping("/init")
@Api(tags = "初始化核心指标数据")
public class InitCoreDataController {

    @Autowired
    private InitCoreDataService service;

    @PostMapping("/coreData")
    @ApiOperation(value = "初始化核心指标数据", notes = "初始化核心指标数据")
    public Resp coreData(@RequestParam("file") MultipartFile file){
        service.initData(file);
        return Resp.success();
    }

}
