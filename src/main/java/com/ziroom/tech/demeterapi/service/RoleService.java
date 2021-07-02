package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.dao.entity.DemeterRole;
import com.ziroom.tech.demeterapi.dao.entity.RoleUser;
import com.ziroom.tech.demeterapi.po.dto.req.role.RoleQueryReq;
import com.ziroom.tech.demeterapi.po.dto.req.role.RoleUserReq;
import java.util.List;
import java.util.Map;

/**
 * @author daijiankun
 */
public interface RoleService {

    void addRole();

    void updateRole();

    void deleteRole();

    List<DemeterRole> queryRoleList(RoleQueryReq roleQueryReq);

    void addRoleUser(RoleUserReq roleUserReq);

    List<DemeterRole> batchQueryByIds(List<Long> roleIds);

    List<RoleUser> batchQueryUserByIds(List<Long> roleIds);

    /**
     * 根据系统号查询所有角色
     * @param uidList 系统号
     * @return Map<String, List<DemeterRole>>
     */
    Map<String, List<DemeterRole>> queryRoleByUid(List<String> uidList);

}
