package com.ziroom.tech.demeterapi.common.api;

import com.alibaba.fastjson.JSONObject;
import com.ziroom.tech.boot.annotation.RetrofitService;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import java.util.Map;

/**
 * @author: liwei
 * @date: 2019/04/23 19:40
 * @description: EHR 接口
 */
@RetrofitService("ehr")
public interface EhrEndPoint {

    /**
     * 根据部门查询部门下所有人员
     * <p>
     * 结果：
     * <p>
     * [
     * {
     * "jobName": "Java工程师",
     * "name": "李威",
     * "jobCode": "100039",
     * "phoneMobile": "13716489624",
     * "username": "20338333"
     * }
     * ]
     * </p>
     *
     * @param deptId 部门code
     * @return 结果
     */
    @GET("/api/ehr/getUsers.action?oper=1")
    Call<JSONObject> getUsers(@Query("deptId") String deptId, @Query("setId") Integer setId);

    /**
     * 根据用户编号查询详情
     *
     * @param userCode 用户编号
     * @return 结果
     */
    @GET("/api/ehr/getUserDetail.action")
    Call<JSONObject> getUserDetail(@Query("userCode") String userCode);

    @GET("/api/ehr/getOrgByCode.action")
    Call<JSONObject> getOrgByCode(@Query("code") String code, @Query("setId") String setId);

    @GET("/api/ehr/getEmpList.action")
    Call<JSONObject> getEmpList(@QueryMap Map<String, Object> empListReqMap);

    @GET("/api/ehr/getAllOrg.action")
    Call<JSONObject> getAllOrgList(@Query("page") int page, @Query("size") int size, @Query("setId") String setId);

    @GET("/api/ehr/getEhrDept.action?level=1")
    Call<JSONObject> getSubordinates(@Query("userCode") String userCode);

    @GET("/api/ehr/getOrgList")
    Call<JSONObject> getOrgList(@Query("city") String city, @Query("orgLevel") int orgLevel, @Query("page") int page,
                                @Query("size") int size);

    @GET("/api/ehr/getOrgList")
    Call<JSONObject> getOrgList(@Query("orgLevel") int orgLevel, @Query("page") int page, @Query("size") int size);

    @GET("/api/ehr/getChildOrgs.action")
    Call<JSONObject> getChildOrgs(@Query("parentId") String parentId, @Query("setId") String setId);

    @GET("/api/ehr/getEhrDept.action")
    Call<JSONObject> getEhrDept(@Query("userCode") String userCode, @Query("level") String level,
                                @Query("flag") Integer flag, @Query("contrastJobCode") String[] contrastJobCode);

    @GET("/api/ehr/getLeaderByCode.action")
    Call<JSONObject> getLeaderByCode(@Query("code") String code, @Query("setId") String setId);

    @GET("/api/ehr/get/jointime")
    Call<JSONObject> getJointime(@Query("empCode") String empCode, @Query("pageNo") Integer pageNo, @Query("pageSize") Integer pageSize);
}
