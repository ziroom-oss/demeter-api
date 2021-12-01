package com.ziroom.tech.demeterapi.open.auth.dao;

import com.ziroom.tech.demeterapi.open.auth.entity.AuthUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xuzeyu
 */
@Mapper
public interface AuthUserMapper {

    List<AuthUserEntity> getRoles(String userCode);

}
