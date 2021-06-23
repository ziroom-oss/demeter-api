package com.ziroom.tech.demeterapi.service.impl;

import com.ziroom.tech.demeterapi.common.exception.BusinessException;
import com.ziroom.tech.demeterapi.dao.entity.DemeterRole;
import com.ziroom.tech.demeterapi.dao.entity.DemeterRoleExample;
import com.ziroom.tech.demeterapi.dao.entity.RoleUser;
import com.ziroom.tech.demeterapi.dao.entity.RoleUserExample;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterRoleDao;
import com.ziroom.tech.demeterapi.dao.mapper.RoleUserDao;
import com.ziroom.tech.demeterapi.po.dto.req.role.RoleQueryReq;
import com.ziroom.tech.demeterapi.po.dto.req.role.RoleUserReq;
import com.ziroom.tech.demeterapi.service.RoleService;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.management.relation.Role;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author daijiankun
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Resource
    private DemeterRoleDao demeterRoleDao;

    @Resource
    private RoleUserDao roleUserDao;


    @Override
    public void addRole() {

    }

    @Override
    public void updateRole() {

    }

    @Override
    public void deleteRole() {

    }

    @Override
    public List<DemeterRole> queryRoleList(RoleQueryReq roleQueryReq) {
        DemeterRoleExample demeterRoleExample = new DemeterRoleExample();
        String queryCondition = roleQueryReq.getQueryCondition();
        if (StringUtils.isNotEmpty(queryCondition)) {
            demeterRoleExample.createCriteria()
                    .andRoleNameLike(queryCondition);
            demeterRoleExample.or().andRoleCodeLike(queryCondition);
        }
        return demeterRoleDao.selectByExample(demeterRoleExample);
    }

    @Override
    public void addRoleUser(RoleUserReq roleUserReq) {
        List<String> systemCodeList = roleUserReq.getSystemCodeList();
        if (CollectionUtils.isNotEmpty(systemCodeList)) {
            systemCodeList.forEach(systemCode -> {
                RoleUser roleUser = new RoleUser();
                roleUser.setRoleId(roleUserReq.getRoleId());
                roleUser.setSystemCode(systemCode);
                roleUser.setCreateTime(new Date());
                roleUser.setUpdateTime(new Date());
                roleUserDao.insertSelective(roleUser);
            });
        } else {
            throw new BusinessException("角色分配的人员不能为空");
        }
    }

    @Override
    public List<DemeterRole> batchQueryByIds(List<Long> roleIds) {
        DemeterRoleExample demeterRoleExample = new DemeterRoleExample();
        demeterRoleExample.createCriteria()
                .andIdIn(roleIds);
        return demeterRoleDao.selectByExample(demeterRoleExample);
    }

    @Override
    public List<RoleUser> batchQueryUserByIds(List<Long> roleIds) {
        RoleUserExample roleUserExample = new RoleUserExample();
        roleUserExample.createCriteria()
                .andRoleIdIn(roleIds);
        return roleUserDao.selectByExample(roleUserExample);
    }
}
