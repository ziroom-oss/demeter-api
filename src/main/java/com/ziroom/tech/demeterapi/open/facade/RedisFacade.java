package com.ziroom.tech.demeterapi.open.facade;

import com.ziroom.tech.demeterapi.open.common.constant.RedisConstants;
import com.ziroom.tech.demeterapi.open.common.model.SopUserRedisStoreModel;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author: xuzeyu
 */
@Slf4j
@Component
@ConditionalOnExpression("!'${spring.profiles.active}'.equals('test')")
public class RedisFacade {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 保持 Session 过期 30 分钟
     */
    public void saveJwt(UserDetailResp userDetailResp) {
        SopUserRedisStoreModel sopUserRedisStoreModel = getSopUserRedisStoreModel(userDetailResp);
        sopUserRedisStoreModel.setRenewTimes(0L);
        sopUserRedisStoreModel.setRenewTimeStamp(System.currentTimeMillis());
        redisTemplate.opsForValue().set(RedisConstants.KEY.DEMETER_FRONT_LOGIN_STRING + userDetailResp.getUserCode(), sopUserRedisStoreModel, RedisConstants.DEMETER_FRONT_LOGIN_EXPIRE, TimeUnit.SECONDS);
    }


    /**
     * 更新缓存
     */
    public void updateRedisStoreUser(SopUserRedisStoreModel user) {
        redisTemplate.opsForValue().set(RedisConstants.KEY.DEMETER_FRONT_LOGIN_STRING + user.getUserCode(), user, RedisConstants.DEMETER_FRONT_LOGIN_EXPIRE,TimeUnit.SECONDS);
    }


    /**
     * 获取 当前用户信息
     */
    public SopUserRedisStoreModel getCurrentUser(String userCode) {
        String key = RedisConstants.KEY.DEMETER_FRONT_LOGIN_STRING + userCode;
        return key == null ? null : (SopUserRedisStoreModel)redisTemplate.opsForValue().get(key);
    }

    /**
     * 续签到45分钟
     */
    public void renew(SopUserRedisStoreModel user) {
        user.setRenewTimeStamp(System.currentTimeMillis());
        user.setRenewTimes(user.getRenewTimes() + 1);
        redisTemplate.opsForValue().set(RedisConstants.KEY.DEMETER_FRONT_LOGIN_STRING + user.getUserCode(), user, RedisConstants.DEMETER_FRONT_LOGIN_EXPIRE,TimeUnit.SECONDS);
    }

    /**
     * 删除缓存的一条记录
     */
    public void deleteRedisStoreUser(String userCode) {
        String key = RedisConstants.KEY.DEMETER_FRONT_LOGIN_STRING + userCode;
        redisTemplate.delete(key);
    }

    private SopUserRedisStoreModel getSopUserRedisStoreModel(UserDetailResp user) {
        SopUserRedisStoreModel sopUserRedisStoreModel = new SopUserRedisStoreModel();
        sopUserRedisStoreModel.setLoginCode(user.getLoginCode());
        sopUserRedisStoreModel.setUserCode(user.getUserCode());
        sopUserRedisStoreModel.setUserName(user.getUserName());
        sopUserRedisStoreModel.setDeptCode(user.getDeptCode());
        return sopUserRedisStoreModel;
    }

}
