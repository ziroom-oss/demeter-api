package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.DemeterTaskUserExtend;
import com.ziroom.tech.demeterapi.dao.entity.DemeterTaskUserExtendExample;
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
public interface DemeterTaskUserExtendDao {

    public int countByExample(DemeterTaskUserExtendExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(DemeterTaskUserExtend record);

    public int insertSelective(DemeterTaskUserExtend record);

    public List<DemeterTaskUserExtend> selectByExample(DemeterTaskUserExtendExample example);

    public DemeterTaskUserExtend selectByPrimaryKey(Long id);

    public DemeterTaskUserExtend selectByTaskUserId(Long taskId);

    public int updateByExampleSelective(@Param("record") DemeterTaskUserExtend record, @Param("example") DemeterTaskUserExtendExample example);

    public int updateByExample(@Param("record") DemeterTaskUserExtend record, @Param("example") DemeterTaskUserExtendExample example);

    public int updateByPrimaryKeySelective(DemeterTaskUserExtend record);

    public int updateByPrimaryKey(DemeterTaskUserExtend record);
}