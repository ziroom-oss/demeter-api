package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.DemeterUserInfo;
import com.ziroom.tech.demeterapi.dao.entity.DemeterUserInfoExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

@Mapper
public interface DemeterUserInfoDao {

    public int countByExample(DemeterUserInfoExample example);

    public int deleteByPrimaryKey(String id);

    public int insert(DemeterUserInfo record);

    public int insertSelective(DemeterUserInfo record);

    public List<DemeterUserInfo> selectByExample(DemeterUserInfoExample example);

    public DemeterUserInfo selectByPrimaryKey(String id);

    public int updateByExampleSelective(@Param("record") DemeterUserInfo record, @Param("example") DemeterUserInfoExample example);

    public int updateByExample(@Param("record") DemeterUserInfo record, @Param("example") DemeterUserInfoExample example);

    public int updateByPrimaryKeySelective(DemeterUserInfo record);

    public int updateByPrimaryKey(DemeterUserInfo record);
}