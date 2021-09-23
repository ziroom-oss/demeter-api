package com.ziroom.tech.demeterapi.controller;

import com.ziroom.gelflog.spring.logger.LogHttpService;
import com.ziroom.tech.demeterapi.service.OamSupportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/api/monthReport")
@Api(tags = "月报")
@LogHttpService
public class MonthReportController {

    @Resource
    private OamSupportService oamSupportService;





}
