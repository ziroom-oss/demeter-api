package com.ziroom.tech.demeterapi.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ziroom.tech.demeterapi.common.api.EhrApiEndPoint;
import com.ziroom.tech.demeterapi.common.utils.RetrofitCallAdaptor;
import com.ziroom.tech.demeterapi.po.dto.req.ehr.EhrEmpListReq;
import com.ziroom.tech.demeterapi.po.dto.req.ehr.EhrOrgListReq;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.stereotype.Component;
import retrofit2.Call;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author huangqiaowei
 * @date 2020-06-08 14:18
 **/
@Slf4j
@Component
public class EhrComponent {

    /**
     * ehr响应中的错误信息属性名
     */
    private final static String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";

    private final static String MESSAGE_ATTRIBUTE = "message";
    /**
     * ehr响应中的错误码属性名
     */
    private final static String ERROR_CODE_ATTRIBUTE = "errorCode";

    private final static String CODE_ATTRIBUTE = "code";
    /**
     * ehr响应中的数据属性名
     */
    private final static String DATA_ATTRIBUTE = "data";
    /**
     * ehr响应中的状态属性名
     */
    private final static String STATUS_ATTRIBUTE = "status";
    /**
     * 默认公司编码
     */
    private final static String DEFAULT_SETID = "101";

    @Resource
    private EhrApiEndPoint ehrApiEndPoint;

    /**
     * 通过部门编码获取部门下的人员列表
     * 如果部门主管在该部门下不是主职的话，不会返回
     *
     * @param deptId 部门编码
     * @return Set<EhrUserResp>
     */
    public Set<EhrUserResp> getUsers(String deptId, Integer setId) {
        log.info("EhrComponent.getUsers params:{} {}", deptId, setId);
        if (Objects.isNull(setId)) {
            setId = 101;
        }
        Call<JSONObject> call = ehrApiEndPoint.getUsers(deptId, setId);
        JSONObject response = RetrofitCallAdaptor.execute(call);
        if (response.getInteger(ERROR_CODE_ATTRIBUTE) != 0) {
            String errorMessage = response.getString(ERROR_MESSAGE_ATTRIBUTE);
            log.error("EhrComponent.getUsers has occurred error message: {}", errorMessage);
            return Sets.newHashSet();
        }
        JSONArray data = response.getJSONArray(DATA_ATTRIBUTE);
        Set<EhrUserResp> userSet = new HashSet<>();
        for (int i = 0, length = data.size(); i < length; i++) {
            JSONObject ehrUser = data.getJSONObject(i);
            EhrUserResp ehrUserResp = new EhrUserResp();
            ehrUserResp.setName(ehrUser.getString("name"));
            ehrUserResp.setUserCode(ehrUser.getString("username"));
            userSet.add(ehrUserResp);
        }
        log.info("EhrComponent.getUsers request success result:{}", userSet);
        return userSet;
    }

    /**
     * 根据用户系统号获取用户详情
     * userCodes最多传10个 示例：["20237106", “20118321”]
     *
     * @param uidString 用户系统号列表
     * @return List<User>
     */
    public List<EhrUserDetailResp> getEhrUserDetail(String uidString) {
        log.info("EhrService.getEhrUserDetail params:{}", uidString);
        List<String> strings = Arrays.asList(uidString.split(","));
        List<String> collect = strings.stream().map(this::addQuotationMark).collect(Collectors.toList());
        String userCodes = "[" +  String.join(",", collect) + "]";
        Call<JSONObject> call = ehrApiEndPoint.getUserDetail(userCodes);
        JSONObject response = RetrofitCallAdaptor.execute(call);
        String failure = "failure";
        if (failure.equals(response.getString(STATUS_ATTRIBUTE))) {
            String errorMessage = response.getString(ERROR_MESSAGE_ATTRIBUTE);
            log.error("EhrService.getEhrUserDetail has occurred error message: {}", errorMessage);
            return Lists.newArrayList();
        }
        JSONArray data = response.getJSONArray(DATA_ATTRIBUTE);
        List<EhrUserDetailResp> userList = Lists.newArrayList();
        for (int i = 0, length = data.size(); i < length; i++) {
            JSONObject ehrUser = data.getJSONObject(i);
            EhrUserDetailResp ehrUserDetailResp = new EhrUserDetailResp();
            ehrUserDetailResp.setEmail(ehrUser.getString("email"));
            ehrUserDetailResp.setAvatar(ehrUser.getString("photo"));
            ehrUserDetailResp.setCode(ehrUser.getString("emplid"));
            ehrUserDetailResp.setName(ehrUser.getString("name"));
            ehrUserDetailResp.setGroupCode(ehrUser.getString("groupCodeNew"));
            ehrUserDetailResp.setGroupName(ehrUser.getString("group"));
            ehrUserDetailResp.setTreePath(ehrUser.getString("treePath"));
            ehrUserDetailResp.setJobIndicator(ehrUser.getString("jobIndicator"));
            ehrUserDetailResp.setLevelName(ehrUser.getString("levelName"));
            ehrUserDetailResp.setDeptName(ehrUser.getString("dept"));
            ehrUserDetailResp.setDeptCode(ehrUser.getString("deptCodeNew"));
            ehrUserDetailResp.setCenter(ehrUser.getString("center"));
            ehrUserDetailResp.setCenterId(ehrUser.getString("centerCode"));
            ehrUserDetailResp.setDesc(ehrUser.getString("descr"));
            ehrUserDetailResp.setJobCodeNew(ehrUser.getString("jobCodeNew"));
            userList.add(ehrUserDetailResp);
        }
        log.info("EhrService.getEhrUserDetail request success result:{}", userList);
        return userList;
    }

    private String addQuotationMark(String uid) {
        return "\"" + uid + "\"";
    }

    /**
     * 根据用户系统号查询名称
     *
     * @param userCodes 用户系统号
     * @return 结果
     */
    public Set<UserResp> getUserDetail(Set<String> userCodes) {
        return Lists.partition(Lists.newArrayList(userCodes), 10).parallelStream().map(strings -> {
            Call<JSONObject> call = ehrApiEndPoint.getUserDetail(strings.toString());
            JSONObject response = RetrofitCallAdaptor.execute(call);
            if (Objects.equals(response.getString("status"), "success")) {
                Set<UserResp> userResps = Sets.newHashSet();
                response.getJSONArray("data").stream().map(o -> (Map) o).forEach(map -> {
                    UserResp userResp = new UserResp();
                    userResp.setCode(Optional.ofNullable(map.get("emplid")).map(Object::toString).orElse(StringUtils.EMPTY));
                    userResp.setName(Optional.ofNullable(map.get("name")).map(Object::toString).orElse(StringUtils.EMPTY));
                    userResp.setEmail(Optional.ofNullable(map.get("email")).map(Object::toString).orElse(StringUtils.EMPTY));
                    userResps.add(userResp);
                });
                return userResps;
            }
            return null;
        }).filter(CollectionUtils::isNotEmpty).reduce((userResps, userResps2) -> {
            userResps.addAll(userResps2);
            return userResps;
        }).orElseGet(Sets::newHashSet);
    }

    /**
     * 模糊查询用户
     *
     * @param ehrEmpListReq req
     * @return 结果
     */
    public List<UserResp> getEmpList(EhrEmpListReq ehrEmpListReq) {
        List<UserResp> resp = Lists.newArrayList();
        Map<String, Object> empReqMap = initReqMap(ehrEmpListReq);
        Call<JSONObject> response = ehrApiEndPoint.getEmpList(empReqMap);

        Optional.ofNullable(RetrofitCallAdaptor.execute(response)).ifPresent(respData -> {
            if (Objects.equals(respData.getString(ERROR_CODE_ATTRIBUTE), "20000")) {
                JSONArray data = respData.getJSONArray(DATA_ATTRIBUTE);
                data.stream().map(o -> JSONObject.parseObject(JSON.toJSONString(o))).forEach(jsonObject -> {
                    UserResp ehrUserResp = new UserResp();
                    ehrUserResp.setName(jsonObject.getString("fullName"));
                    ehrUserResp.setCode(jsonObject.getString("empCode"));
                    ehrUserResp.setEmail(jsonObject.getString("email"));
                    resp.add(ehrUserResp);
                });
            }
        });
        return resp;
    }

    /**
     * 查询用户详情
     *
     * @param userCode code
     * @return 结果
     */
    public UserDetailResp getUserDetail(String userCode) {
        Call<JSONObject> response = ehrApiEndPoint.getUserDetail(Lists.newArrayList(userCode).toString());
        UserDetailResp userDetailResp = new UserDetailResp();
        Optional.ofNullable(RetrofitCallAdaptor.execute(response)).ifPresent(respData -> {
            if (Objects.equals(respData.getString(ERROR_CODE_ATTRIBUTE), "20000")) {
                JSONArray data = respData.getJSONArray(DATA_ATTRIBUTE);
                data.stream().map(o -> JSONObject.parseObject(JSON.toJSONString(o))).filter(jsonObject -> Objects.equals(jsonObject.getString(
                        "jobIndicator"), "P")).forEach(jsonObject -> {
                    userDetailResp.setJob(jsonObject.getString("descr"));
                    userDetailResp.setDept(jsonObject.getString("dept"));
                    userDetailResp.setDeptCode(jsonObject.getString("deptCode"));
                    userDetailResp.setGroup(jsonObject.getString("group"));
                    userDetailResp.setPhone(jsonObject.getString("phone"));
                    userDetailResp.setEmail(jsonObject.getString("email"));
                    userDetailResp.setUserCode(jsonObject.getString("emplid"));
                    userDetailResp.setUserName(jsonObject.getString("name"));
                    userDetailResp.setHighestEducation(jsonObject.getString("highestEducation"));
                    userDetailResp.setLevelName(jsonObject.getString("levelName"));
                    userDetailResp.setPhoto(jsonObject.getString("photo"));
                    userDetailResp.setEffdt(jsonObject.getString("effdt"));
                    userDetailResp.setTreePath(jsonObject.getString("treePath"));
                });
            }
        });
        return userDetailResp;
    }

    /**
     * 根据条件查询部门列表，目前仅用来查询部门树的根节点
     *
     * @return Set<EhrDeptResp>
     */
    public Set<EhrDeptResp> getOrgList(EhrOrgListReq ehrOrgListReq) {
        log.info("EhrComponent.getOrgList params:{}", JSON.toJSONString(ehrOrgListReq));
        Call<JSONObject> call = ehrApiEndPoint.getOrgList(ehrOrgListReq.getOrgLevel(), ehrOrgListReq.getPage(), ehrOrgListReq.getSize());
        JSONObject response = RetrofitCallAdaptor.execute(call);
        String success = "20000";
        if (!success.equals(response.getString(ERROR_CODE_ATTRIBUTE))) {
            String errorMessage = response.getString(ERROR_MESSAGE_ATTRIBUTE);
            log.error("EhrService.getOrgList has occurred error message: {}", errorMessage);
            return Sets.newHashSet();
        }
        JSONArray data = response.getJSONArray(DATA_ATTRIBUTE);
        Set<EhrDeptResp> deptSet = new HashSet<>();
        for (int i = 0, length = data.size(); i < length; i++) {
            JSONObject dept = data.getJSONObject(i);
            EhrDeptResp ehrDeptResp = new EhrDeptResp();
            // 这里取部门新编码
            ehrDeptResp.setCode(dept.getString("orgCode"));
            ehrDeptResp.setName(dept.getString("orgName"));
            deptSet.add(ehrDeptResp);
        }
//        log.info("EhrService.getOrgList request success result:{}", deptSet);
        return deptSet;
    }

    public Set<EhrDeptResp> getAllThirdOrgList() {
        log.info("EhrComponent.getAllThirdOrgList");
        int size = 100, level = 3;
        Call<JSONObject> call = ehrApiEndPoint.getOrgList(level, 1, size);
        JSONObject response = RetrofitCallAdaptor.execute(call);
        Set<EhrDeptResp> result = Sets.newHashSet();
        String success = "20000";
        if (!success.equals(response.getString(ERROR_CODE_ATTRIBUTE))) {
            String errorMessage = response.getString(ERROR_MESSAGE_ATTRIBUTE);
            log.error("EhrComponent.getAllThirdOrgList has occurred error message: {}", errorMessage);
            return result;
        }
        JSONArray data = response.getJSONArray(DATA_ATTRIBUTE);
        for (int i = 0, length = data.size(); i < length; i++) {
            JSONObject dept = data.getJSONObject(i);
            EhrDeptResp ehrDeptResp = new EhrDeptResp();
            // 这里取部门新编码
            ehrDeptResp.setCode(dept.getString("orgCode"));
            ehrDeptResp.setName(dept.getString("orgName"));
            result.add(ehrDeptResp);
        }
        int totleCount = response.getInteger("totleCount") - size;
        int loopCount = (int) Math.ceil((double) totleCount / size);
        List<CompletableFuture<Void>> futureList = new ArrayList<>(loopCount);
        for (int page = 1; page <= loopCount; page++) {
            // 生成一个CompletableFuture来访问ehr接口
            int finalPage = page;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                EhrOrgListReq req = new EhrOrgListReq();
                req.setOrgLevel(level);
                req.setPage(finalPage + 1);
                req.setSize(size);
                result.addAll(getOrgList(req));
            }).exceptionally(throwable -> null);
            futureList.add(future);
        }
        // 通过allOf方法实现异步执行后，同步搜集结果，这里没有超时控制
        CompletableFuture<Void> allFuture = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
        allFuture.thenApply(e -> futureList.stream().map(CompletableFuture::join).collect(Collectors.toList())).join();
        log.info("EhrService.getAllThirdOrgList request success result:{}", result);
        return result;
    }

    public EhrDeptResp getCurrentThirdDept() {
        List<EhrUserDetailResp> ehrUserDetail = getEhrUserDetail("[" + OperatorContext.getOperator() + "]");
        if (!CollectionUtils.isEmpty(ehrUserDetail)) {
            for (EhrUserDetailResp ehrUser : ehrUserDetail) {
                if (ehrUser.getJobIndicator().equals("P")) {
                    // 主职
                    String treePath = ehrUser.getTreePath();
                    String[] treePaths = treePath.split(",");
                    EhrDeptResp resp = new EhrDeptResp();
                    if (treePaths.length >= 5) {
                        String thirdDept = treePaths[4];
                        resp.setCode(thirdDept);
                    }
                    return resp;
                }
            }
        }
        return new EhrDeptResp();
    }

    public String getPrincipalDept() {
        List<EhrUserDetailResp> ehrUserDetail = getEhrUserDetail("[" + OperatorContext.getOperator() + "]");
        if (!CollectionUtils.isEmpty(ehrUserDetail)) {
            for (EhrUserDetailResp ehrUser : ehrUserDetail) {
                if (ehrUser.getJobIndicator().equals("P")) {
                    // 只截取 平台-中心-部门，其他接口请勿使用
                    String treePath = ehrUser.getTreePath();
                    List<String> treeList = Arrays.asList(treePath.split(","));
                    List<String> rtv = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(treeList)) {
                        if (treeList.size() == 3 || treeList.size() == 4 || treeList.size() == 5) {
                            rtv = treeList.subList(2, treeList.size());
                        } else if (treeList.size() == 6) {
                            rtv = treeList.subList(2, 5);
                        }
                        return String.join(",", rtv);
                    }
                }
            }
        }
        return null;
    }

    /**
     * 通过部门编码和公司编码查询指定部门信息
     *
     * @param deptCode 部门编码
     * @param setId    公司编码
     * @return EhrDeptResp
     */
    public EhrDeptResp getOrgByCode(String deptCode, String setId) {
        if (StringUtils.isBlank(setId)) {
            setId = DEFAULT_SETID;
        }
        // 目前公司编码只支持总部Ta
        log.info("EhrService.getOrgByCode params:{} {}", deptCode, setId);
        Call<JSONObject> call = ehrApiEndPoint.getOrgByCode(deptCode, setId);
        JSONObject response = RetrofitCallAdaptor.execute(call);
        String failure = "failure";
        if (failure.equals(response.getString(STATUS_ATTRIBUTE))) {
            String errorMessage = response.getString(ERROR_MESSAGE_ATTRIBUTE);
            log.error("EhrService.getOrgByCode has occurred error message: {}", errorMessage);
            return null;
        }
        JSONObject data = response.getJSONObject(DATA_ATTRIBUTE);
        EhrDeptResp ehrDeptResp = new EhrDeptResp();
        ehrDeptResp.setName(data.getString("descrShort"));
        ehrDeptResp.setCode(data.getString("deptid"));
        log.info("EhrService.getOrgByCode request success result:{}", ehrDeptResp);
        return ehrDeptResp;
    }

    /**
     * 通过父部门id和公司编码获取子部门
     *
     * @param parentId 父部门id
     * @param setId    公司编码
     * @return Set<EhrDeptResp>
     */
    public Set<EhrDeptResp> getChildOrgs(String parentId, String setId) {
        log.info("EhrService.getChildOrgs params:{} {}", parentId, setId);
        Call<JSONObject> call = ehrApiEndPoint.getChildOrgs(parentId, setId);
        JSONObject response = RetrofitCallAdaptor.execute(call);
        if (response.getInteger(ERROR_CODE_ATTRIBUTE) != 0) {
            String errorMessage = response.getString(ERROR_MESSAGE_ATTRIBUTE);
            log.error("EhrService.getChildOrgs has occurred error message: {}", errorMessage);
            return Sets.newHashSet();
        }
        JSONArray data = response.getJSONArray(DATA_ATTRIBUTE);
        Set<EhrDeptResp> deptSet = new HashSet<>();
        for (int i = 0, length = data.size(); i < length; i++) {
            JSONObject dept = data.getJSONObject(i);
            EhrDeptResp ehrDeptResp = new EhrDeptResp();
            // 这里取部门新编码
            ehrDeptResp.setCode(dept.getString("newCode"));
            ehrDeptResp.setName(dept.getString("name"));
            deptSet.add(ehrDeptResp);
        }
        log.info("EhrService.getChildOrgs request success result:{}", deptSet);
        return deptSet;
    }

    /**
     * 根据部门编码列表获取部门信息
     *
     * @param deptCodeList 部门编码列表
     * @param setId        公司编码 目前仅支持北京自如
     * @return key为部门编码，value为部门信息的map
     * 传入的deptCodeList为空或者size为0，直接返回空集合
     */
    public Map<String, EhrDeptResp> getOrgByCodeList(List<String> deptCodeList, String setId) {
        log.info("EhrService.getOrgByCodeList params:{} {}", deptCodeList, setId);
        Map<String, EhrDeptResp> ehrDeptRespMap = new HashMap<>(deptCodeList.size());
        // 传入的deptCodeList为空或者size为0，直接返回空集合
        if (org.springframework.util.CollectionUtils.isEmpty(deptCodeList)) {
            return ehrDeptRespMap;
        }
        List<CompletableFuture<Void>> futureList = new ArrayList<>(deptCodeList.size());
        for (String deptCode : deptCodeList) {
            // 生成一个CompletableFuture来访问ehr接口
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                EhrDeptResp ehrDeptResp = getOrgByCode(deptCode, setId);
                ehrDeptRespMap.put(deptCode, ehrDeptResp);
            }).exceptionally(throwable -> {
                ehrDeptRespMap.put(deptCode, null);
                return null;
            });
            futureList.add(future);
        }
        // 通过allOf方法实现异步执行后，同步搜集结果，这里没有超时控制
        CompletableFuture<Void> allFuture = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
        allFuture.thenApply(e -> futureList.stream().map(CompletableFuture::join).collect(Collectors.toList())).join();
        log.info("EhrService.getOrgByCodeList request success result:{}", ehrDeptRespMap);
        return ehrDeptRespMap;
    }

    public List<EhrJoinTimeResp> getJointime(String empCode) {
        Call<JSONObject> call = ehrApiEndPoint.getJointime(empCode, 1, 10);
        JSONObject response = RetrofitCallAdaptor.execute(call);
        String success = "20000";
        if (!success.equals(response.getString(ERROR_CODE_ATTRIBUTE))) {
            String errorMessage = response.getString(MESSAGE_ATTRIBUTE);
            log.error("EhrComponent.getJointime has occurred error message: {}", errorMessage);
            return null;
        }
        JSONObject data = response.getJSONObject(DATA_ATTRIBUTE);
        JSONArray list = data.getJSONArray("list");
        List<EhrJoinTimeResp> respList = new ArrayList<>(16);
        for (int i = 0; i < list.size(); i++) {
            JSONObject jsonObject = list.getJSONObject(i);
            EhrJoinTimeResp ehrJoinTime = new EhrJoinTimeResp();
            ehrJoinTime.setEmpCode(jsonObject.getString("empCode"));
            ehrJoinTime.setEntryTime(jsonObject.getString("entryTime"));
            respList.add(ehrJoinTime);
        }
        return respList;
    }


    private Map<String, Object> initReqMap(Object o) {
        Map<String, Object> result = Maps.newHashMap();
        FieldUtils.getAllFieldsList(o.getClass()).forEach(field -> {
            try {
                if (Objects.nonNull(FieldUtils.readDeclaredField(o, field.getName(), true))) {
                    result.put(field.getName(), FieldUtils.readDeclaredField(o, field.getName(), true));
                }
            } catch (IllegalAccessException e) {
                // ignore
            }
        });
        return result;
    }

}
