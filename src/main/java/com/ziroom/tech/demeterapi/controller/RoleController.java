package com.ziroom.tech.demeterapi.controller;

import com.ziroom.tech.demeterapi.dao.entity.DemeterRole;
import com.ziroom.tech.demeterapi.po.dto.Resp;
import com.ziroom.tech.demeterapi.po.dto.req.role.RoleQueryReq;
import com.ziroom.tech.demeterapi.service.RoleService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author daijiankun
 */
@RestController
@Slf4j
@RequestMapping("api/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @PostMapping(value = "query")
    @ApiOperation(value = "查询角色列表", httpMethod = "POST")
    public Resp<List<DemeterRole>> queryRoleList(RoleQueryReq roleQueryReq) {
        return Resp.success(roleService.queryRoleList(roleQueryReq));
    }
}
