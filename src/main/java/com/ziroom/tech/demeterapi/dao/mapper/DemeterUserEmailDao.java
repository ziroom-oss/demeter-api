package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.DemeterUserEmail;
import com.ziroom.tech.demeterapi.dao.entity.DemeterUserEmailExample;
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
public interface DemeterUserEmailDao {

    public int countByExample(DemeterUserEmailExample example);

    public int deleteByPrimaryKey(String id);

    public int insert(DemeterUserEmail record);

    public int insertSelective(DemeterUserEmail record);

    public List<DemeterUserEmail> selectByExample(DemeterUserEmailExample example);

    public DemeterUserEmail selectByPrimaryKey(String id);

    public int updateByExampleSelective(@Param("record") DemeterUserEmail record, @Param("example") DemeterUserEmailExample example);

    public int updateByExample(@Param("record") DemeterUserEmail record, @Param("example") DemeterUserEmailExample example);

    public int updateByPrimaryKeySelective(DemeterUserEmail record);

    public int updateByPrimaryKey(DemeterUserEmail record);
}