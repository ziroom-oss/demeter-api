package com.ziroom.tech.demeterapi.open.ehr.client.service;

import com.google.common.collect.Lists;
import com.ziroom.tech.demeterapi.open.model.ModelResult;
import com.ziroom.tech.demeterapi.open.utils.ModelResultUtil;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserResp;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 用户服务
 * @author xuzeyu
 */
@Service
public class EhrServiceClient {

    /**
     * 根据用户系统号查询用户名称
     */
    public ModelResult<List<UserResp>> getUserDetail(List<String> userCodes) {
        List<UserResp> userRespList = Lists.newArrayList();
        for(String userCode: userCodes){
            UserResp userResp = new UserResp();
            userResp.setCode(userCode);
            userResp.setName("徐泽宇");
            userResp.setEmail("xuzy5@ziroom.com");
            userRespList.add(userResp);
        }

        return ModelResultUtil.success(userRespList.stream().filter(distinctByKey(UserResp::getCode)).collect(Collectors.toList()));
    }

    /**
     * 查询用户信息
     */
    public ModelResult<UserDetailResp> getUserInfo(String userCode){
        UserDetailResp userDetailResp = new UserDetailResp();
        userDetailResp.setUserCode(userCode);
        userDetailResp.setUserName("徐泽宇");
        userDetailResp.setDept("xxx");
        userDetailResp.setDeptCode("xxx");
        userDetailResp.setEmail("xuzy5@ziroom.com");
        return ModelResultUtil.success(userDetailResp);
    }

    public static <T> Predicate<T> distinctByKey(Function<T, ?> keyExtractor) {
        Map<Object, Boolean> map = new HashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
