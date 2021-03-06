package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.RoleUser;
import com.ziroom.tech.demeterapi.dao.entity.RoleUserExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

@Mapper
public interface RoleUserDao {

    public int countByExample(RoleUserExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(RoleUser record);

    public int insertSelective(RoleUser record);

    public List<RoleUser> selectByExample(RoleUserExample example);

    public RoleUser selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") RoleUser record, @Param("example") RoleUserExample example);

    public int updateByExample(@Param("record") RoleUser record, @Param("example") RoleUserExample example);

    public int updateByPrimaryKeySelective(RoleUser record);

    public int updateByPrimaryKey(RoleUser record);
}