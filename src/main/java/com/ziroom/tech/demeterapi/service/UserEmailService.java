package com.ziroom.tech.demeterapi.service;

import com.ziroom.tech.demeterapi.po.dto.req.email.UserEmailDto;
import com.ziroom.tech.demeterapi.po.qo.UserEmailQo;

import java.util.List;

/**
 * @author libingsi
 * @date 2021/8/4 17:22
 */
public interface UserEmailService {

    /**
     * 维护用户邮箱
     * @param qo
     * @return
     */
    Integer insertEmail(UserEmailQo qo);

    /**
     * 删除用户子邮箱
     * @param id
     * @return
     */
    Integer deleteEmail(Long id);

    /**
     * 查询用户子邮箱
     * @param qo
     * @return
     */
    List<UserEmailDto> selectSubEmail(UserEmailQo qo);
}
