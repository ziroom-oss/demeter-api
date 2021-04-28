package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.TaskFinishConditionInfo;
import com.ziroom.tech.demeterapi.dao.entity.TaskFinishConditionInfoExample;
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
public interface TaskFinishConditionInfoDao {

    public int countByExample(TaskFinishConditionInfoExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(TaskFinishConditionInfo record);

    public int insertSelective(TaskFinishConditionInfo record);

    public List<TaskFinishConditionInfo> selectByExample(TaskFinishConditionInfoExample example);

    public TaskFinishConditionInfo selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") TaskFinishConditionInfo record, @Param("example") TaskFinishConditionInfoExample example);

    public int updateByExample(@Param("record") TaskFinishConditionInfo record, @Param("example") TaskFinishConditionInfoExample example);

    public int updateByPrimaryKeySelective(TaskFinishConditionInfo record);

    public int updateByPrimaryKey(TaskFinishConditionInfo record);
}