package com.ziroom.tech.demeterapi.open.ehr.client.service;

import com.google.common.collect.Lists;
import com.ziroom.tech.demeterapi.open.common.enums.ResponseEnum;
import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.common.utils.ModelResultUtil;
import com.ziroom.tech.demeterapi.open.login.param.LogInUserParam;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.EhrDeptResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;
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

    /**
     * 根部门信息
     */
    EhrDeptResp rootDept = new EhrDeptResp("101", "自如");

    //二级部门
    EhrDeptResp Level2Dept1 = new EhrDeptResp("201", "互联网平台");
    EhrDeptResp Level2Dept2 = new EhrDeptResp("202", "业务中心");

    //三级部门
    EhrDeptResp Level3Dept1 = new EhrDeptResp("102558", "基础平台组");
    EhrDeptResp Level3Dept2 = new EhrDeptResp("102550", "业务平台组");

    /**
     * 部门map
     */
    Map<String, EhrDeptResp> deptMap = new HashMap<String, EhrDeptResp>(){{
        put("101", rootDept);
        put("201", Level2Dept1);
        put("202", Level2Dept2);
        put("102558", Level3Dept1);
        put("102550", Level3Dept2);
    }};

    /**
     * 下级部门
     */
    Map<String, List<EhrDeptResp>> lowerDept = new HashMap<String, List<EhrDeptResp>>(){{
        put("101", Arrays.asList(Level2Dept1, Level2Dept2));
        put("201", Arrays.asList(Level3Dept1));
        put("202", Arrays.asList(Level3Dept2));
    }};

    /**
     * 部门下人员信息
     */
    Map<String, List<UserDetailResp>> userRelDeptMap = new HashMap<String, List<UserDetailResp>>(){{
        List<UserDetailResp> dept1List = Lists.newArrayList();
        dept1List.add(new UserDetailResp("60010370","杨天佑","yangty1@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        dept1List.add(new UserDetailResp("60033587","徐泽宇","xuzy5@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        dept1List.add(new UserDetailResp("60028724","代建坤","daijk@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        dept1List.add(new UserDetailResp("60022930","戴锦如","daijr@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        dept1List.add(new UserDetailResp("60007423","梁仁凯","liangrk@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        dept1List.add(new UserDetailResp("60034198","章鑫童","zhangxt3@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        put("102558", dept1List);

        List<UserDetailResp> dept2List = Lists.newArrayList();
        dept2List.add(new UserDetailResp("60050000","狄仁杰","dirj1@ziroom.com", "北方业务拓展", "102550", "101/202/102550"));
        dept2List.add(new UserDetailResp("60050001","李元芳","liyf1@ziroom.com", "北方业务拓展", "102550", "101/202/102550"));
        put("102550", dept2List);
    }};


    /**
     * 员工信息
     */
    Map<String, UserDetailResp> userMap = new HashMap<String, UserDetailResp>(){{
        put("60010370", new UserDetailResp("60010370","杨天佑","yangty1@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        put("60033587", new UserDetailResp("60033587","徐泽宇","xuzy5@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        put("60028724", new UserDetailResp("60028724","代建坤","daijk@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        put("60022930", new UserDetailResp("60022930","戴锦如","daijr@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        put("60007423", new UserDetailResp("60007423","梁仁凯","liangrk@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        put("60034198", new UserDetailResp("60034198","章鑫童","zhangxt3@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        put("60050000", new UserDetailResp("60050000","狄仁杰","dirj1@ziroom.com", "北方业务拓展", "102550", "101/202/102550"));
        put("60050001", new UserDetailResp("60050001","李元芳","liyf1@ziroom.com", "北方业务拓展", "102550", "101/202/102550"));
    }};

    Map<String, UserDetailResp> userMap1 = new HashMap<String, UserDetailResp>(){{
        put("杨天佑", new UserDetailResp("60010370","杨天佑","yangty1@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        put("徐泽宇", new UserDetailResp("60033587","徐泽宇","xuzy5@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        put("代建坤", new UserDetailResp("60028724","代建坤","daijk@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        put("戴锦如", new UserDetailResp("60022930","戴锦如","daijr@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        put("梁仁凯", new UserDetailResp("60007423","梁仁凯","liangrk@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        put("章鑫童", new UserDetailResp("60034198","章鑫童","zhangxt3@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        put("狄仁杰", new UserDetailResp("60050000","狄仁杰","dirj1@ziroom.com", "北方业务拓展", "102550", "101/202/102550"));
        put("李元芳", new UserDetailResp("60050001","李元芳","liyf1@ziroom.com", "北方业务拓展", "102550", "101/202/102550"));
    }};

    Map<String, UserDetailResp> userLoginMap = new HashMap<String, UserDetailResp>(){{
        put("yangty1", new UserDetailResp("60010370","杨天佑","yangty1@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        put("xuzy5", new UserDetailResp("60033587","徐泽宇","xuzy5@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        put("daijk", new UserDetailResp("60028724","代建坤","daijk@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        put("daijr", new UserDetailResp("60022930","戴锦如","daijr@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        put("liangrk", new UserDetailResp("60007423","梁仁凯","liangrk@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        put("zhangxt3", new UserDetailResp("60034198","章鑫童","zhangxt3@ziroom.com", "基础平台组", "102558", "101/201/102558"));
        put("dirj1", new UserDetailResp("60050000","狄仁杰","dirj1@ziroom.com", "北方业务拓展", "102550", "101/202/102550"));
        put("liyf1", new UserDetailResp("60050001","李元芳","liyf1@ziroom.com", "北方业务拓展", "102550", "101/202/102550"));
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
     * 登录专属 根据登录名/密码查询
     */
    public ModelResult<UserDetailResp> getUserInfoByLogin(LogInUserParam loginParam){
        UserDetailResp userDetailResp = userMap.get(loginParam.getLoginName());
        if(Objects.isNull(userDetailResp)){
          ModelResultUtil.error(ResponseEnum.FRONT_LOGIN_USER_PASSWORD_WRONG.getCode(),ResponseEnum.FRONT_LOGIN_USER_PASSWORD_WRONG.getMessage());
        }
        return ModelResultUtil.success(userDetailResp);
    }

    /**
     * 根据用户唯一标识code查询用户信息
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
            tempUser.setTreePath("101/201/102558");
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

    /**
     * 根据提交查询员工信息
     */
    public ModelResult<List<UserDetailResp>> getAllUsers(String empCodeNameAdcode){
        UserDetailResp userDetailResp = userMap1.get(empCodeNameAdcode);
        if(Objects.isNull(userDetailResp)){
            return ModelResultUtil.error("-1","未查询到用户");
        }
        return ModelResultUtil.success(Collections.singletonList(userDetailResp));
    }

    /**
     * 查询部门根信息
     */
    public ModelResult<EhrDeptResp> getRootDept(){
        return ModelResultUtil.success(rootDept);
    }

    /**
     * 根据部门code查询人员信息
     */
    public ModelResult<List<UserDetailResp>> getEmpListByDept(String deptCode){
        List<UserDetailResp> userDetailRespList = userRelDeptMap.get(deptCode);
        if(CollectionUtils.isEmpty(userDetailRespList)){
            return ModelResultUtil.error("-1","未查询到人员信息");
        }
        return ModelResultUtil.success(userDetailRespList);
    }

    /**
     * 根据部门code查询下级部门
     */
    public ModelResult<List<EhrDeptResp>> getChildDept(String deptCode){
        List<EhrDeptResp> ehrDeptResps = lowerDept.get(deptCode);
        return ModelResultUtil.success(ehrDeptResps);
    }

    /**
     * 查询部门信息
     */
    public ModelResult<EhrDeptResp> getDeptInfo(String deptCode){
        EhrDeptResp ehrDeptResp = deptMap.get(deptCode);
        return ModelResultUtil.success(ehrDeptResp);
    }

    public static <T> Predicate<T> distinctByKey(Function<T, ?> keyExtractor) {
        Map<Object, Boolean> map = new HashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
