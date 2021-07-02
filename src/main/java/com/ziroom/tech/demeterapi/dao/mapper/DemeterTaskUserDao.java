package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.DemeterTaskUser;
import com.ziroom.tech.demeterapi.dao.entity.DemeterTaskUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

/**
 * <pre>
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑, 永无BUG!
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * </pre>
 */
@SuppressWarnings("UnnecessaryInterfaceModifier")
@Repository
public interface DemeterTaskUserDao {

    public int countByExample(DemeterTaskUserExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(DemeterTaskUser record);

    public int insertSelective(DemeterTaskUser record);

    public List<DemeterTaskUser> selectByExample(DemeterTaskUserExample example);

    public DemeterTaskUser selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") DemeterTaskUser record, @Param("example") DemeterTaskUserExample example);

    public int updateByExample(@Param("record") DemeterTaskUser record, @Param("example") DemeterTaskUserExample example);

    public int updateByPrimaryKeySelective(DemeterTaskUser record);

    public int updateByPrimaryKey(DemeterTaskUser record);
}