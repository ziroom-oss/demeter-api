package com.ziroom.tech.demeterapi.dao.mapper;

import com.ziroom.tech.demeterapi.dao.entity.TaskFinishCondition;
import com.ziroom.tech.demeterapi.dao.entity.TaskFinishConditionExample;
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
public interface TaskFinishConditionDao {

    public int countByExample(TaskFinishConditionExample example);

    public int deleteByPrimaryKey(Long id);

    public int insert(TaskFinishCondition record);

    public int insertSelective(TaskFinishCondition record);

    public List<TaskFinishCondition> selectByExample(TaskFinishConditionExample example);

    public TaskFinishCondition selectByPrimaryKey(Long id);

    public int updateByExampleSelective(@Param("record") TaskFinishCondition record, @Param("example") TaskFinishConditionExample example);

    public int updateByExample(@Param("record") TaskFinishCondition record, @Param("example") TaskFinishConditionExample example);

    public int updateByPrimaryKeySelective(TaskFinishCondition record);

    public int updateByPrimaryKey(TaskFinishCondition record);
}