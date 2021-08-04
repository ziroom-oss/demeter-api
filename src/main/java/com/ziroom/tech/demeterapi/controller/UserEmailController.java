package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.common.BeanMapper;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.email.UserEmailDto;
import com.ziroom.tech.demeterapi.po.qo.UserEmailQo;
import com.ziroom.tech.demeterapi.po.vo.UserEmailVo;
import com.ziroom.tech.demeterapi.service.UserEmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author libingsi
 * @date 2021/8/4 17:22
 */
@RestController
@RequestMapping("/api/email")
@Api(value = "维护用户邮箱", tags = "维护用户邮箱")
@Slf4j
public class UserEmailController {

    @Autowired
    private UserEmailService emailService;
    @Autowired
    private BeanMapper beanMapper;


    @PostMapping("/selectSubEmail")
    @ApiOperation(value = "查询用户邮箱",notes = "查询用户邮箱")
    public Resp<List<UserEmailVo>> selectSubEmail(@RequestBody UserEmailQo qo) {
        List<UserEmailDto> dto = emailService.selectSubEmail(qo);
        List<UserEmailVo> vo = beanMapper.mapList(dto,UserEmailVo.class);
        return Resp.success(vo);
    }

    @PostMapping("/insertEmail")
    @ApiOperation(value = "维护用户邮箱",notes = "维护用户子邮箱")
    public Resp<Integer> insertEmail(@RequestBody UserEmailQo qo) {
        Integer integer = emailService.insertEmail(qo);
        return Resp.success(integer);
    }

    @PostMapping("/deleteEmail")
    @ApiOperation(value = "删除用户邮箱",notes = "删除用户子邮箱")
    public Resp<Integer> deleteEmail(@RequestParam Long id) {
        Integer integer = emailService.deleteEmail(id);
        return Resp.success(integer);
    }


}
