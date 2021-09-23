package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.service.OamSupportService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/api/oamSupport")
@ApiOperation("Operation and maintenance 运维支持数据")
public class OamSupportController {

    @Resource
    private OamSupportService oamSupportService;





}
