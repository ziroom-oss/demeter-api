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
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 用户服务
 * 此处需要替换为自己的用户中心
 * @author xuzeyu
 */
@Service
public class EhrServiceClient {

    Map<String, UserDetailResp> userMap = new HashMap<String, UserDetailResp>(){{
        put("60010370", new UserDetailResp("60010370","杨天佑","yangty1@ziroom.com", "基础平台组", "102558"));
        put("60033587", new UserDetailResp("60033587","徐泽宇","xuzy5@ziroom.com", "基础平台组", "102558"));
        put("60028724", new UserDetailResp("60028724","代建坤","daijk@ziroom.com", "基础平台组", "102558"));
        put("60022930", new UserDetailResp("60022930","戴锦如","daijr@ziroom.com", "基础平台组", "102558"));
        put("60007423", new UserDetailResp("60007423","梁仁凯","liangrk@ziroom.com", "基础平台组", "102558"));
    }};


    /**
     * 根据用户系统号查询用户名称
     */
    public ModelResult<List<UserDetailResp>> getUserDetail(List<String> userCodes) {
        List<UserDetailResp> userRespList = Lists.newArrayList();
        for(String userCode: userCodes){
            if(userMap.containsKey(userCode)){
                UserDetailResp userResp = new UserDetailResp();
                userResp.setUserCode(userCode);
                userResp.setUserName(userMap.get(userCode).getUserName());
                userResp.setEmail(userMap.get(userCode).getEmail());
                userRespList.add(userResp);
            } else {
                UserDetailResp userResp = new UserDetailResp();
                userResp.setUserCode(userCode);
                userResp.setUserName("徐泽宇");
                userResp.setEmail("xuzy5@ziroom.com");
                userRespList.add(userResp);
            }

        }
        return ModelResultUtil.success(userRespList.stream().filter(distinctByKey(UserDetailResp::getUserCode)).collect(Collectors.toList()));
    }

    /**
     * 查询用户信息
     */
    public ModelResult<UserDetailResp> getUserInfo(String userCode){
        UserDetailResp userDetailResp = userMap.get(userCode);
        if(Objects.isNull(userDetailResp)){
            UserDetailResp tempUser = new UserDetailResp();
            tempUser.setUserCode(userCode);
            tempUser.setUserName("徐泽宇");
            tempUser.setEmail("xuzy5@ziroom.com");
            tempUser.setDept("基础平台组");
            tempUser.setDeptCode("102558");
            return ModelResultUtil.success(tempUser);
        }
        return ModelResultUtil.success(userDetailResp);
    }

    /**
     * 查询部门下成员信息
     */
    public ModelResult<List<UserDetailResp>> getUsersByDept(String deptCode){
        List<UserDetailResp> userDetailResps = userMap.values().stream().collect(Collectors.toList());
        return ModelResultUtil.success(userDetailResps);
    }

    public static <T> Predicate<T> distinctByKey(Function<T, ?> keyExtractor) {
        Map<Object, Boolean> map = new HashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
