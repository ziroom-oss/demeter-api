package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.Jobs;
import com.ziroom.tech.demeterapi.dao.entity.JobsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

@SuppressWarnings("UnnecessaryInterfaceModifier")
@Repository
public interface JobsDao {

    /**
     * 移除指定 id 的职务
     */
    public int deleteByPrimaryKey(Long id);

    /**
     * 新建职务
     */
    public int insertSelective(Jobs record);

    /**
     * 查询所有职务
     */
    public List<Jobs> selectAll();

    /**
     * 按 id 查询职务
     */
    public Jobs selectByPrimaryKey(Long id);

    /**
     * 按 code 查询职务
     */
    public Jobs selectByCode(@Param("code") Integer code);

    /**
     * 更新指定 id 的职务
     */
    public int updateByPrimaryKeySelective(Jobs record);
}