package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.DemeterRole;
import com.ziroom.tech.demeterapi.dao.entity.DemeterRoleExample;
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
public interface DemeterRoleDao {

    public int countByExample(DemeterRoleExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(DemeterRole record);

    public int insertSelective(DemeterRole record);

    public List<DemeterRole> selectByExample(DemeterRoleExample example);

    public DemeterRole selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") DemeterRole record, @Param("example") DemeterRoleExample example);

    public int updateByExample(@Param("record") DemeterRole record, @Param("example") DemeterRoleExample example);

    public int updateByPrimaryKeySelective(DemeterRole record);

    public int updateByPrimaryKey(DemeterRole record);
}