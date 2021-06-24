package com.ziroom.tech.demeterapi.common.api;

import com.alibaba.fastjson.JSONObject;
import com.ziroom.tech.boot.annotation.RetrofitService;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author daijiankun
 */
@RetrofitService("ca")
public interface CodeAnalysisApiEndPoint {


    @GET("/report/getDE")
    Call<JSONObject> getDevelopmentEquivalent(@Query("account") String email, @Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("/report/getDE")
    Call<JSONObject> getSingleDE(@Query("account") String email, @Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("/report/getDEByDepartment")
    Call<JSONObject> getDEByDepartment(@Query("departmentCode") String departmentCode, @Query("account") String account, @Query("fromDate") String fromDate, @Query("toDate") String toDate);

    /**
     * 部门工程指标统计
     * @param departmentCode 部门code
     * @param fromDate 开始时间
     * @param toDate 结束时间
     * @return Call<JSONObject>
     */
    @POST("/report/getDepartmentDe")
    Call<JSONObject> getDepartmentDe(@Query("departmentCode") String departmentCode, @Query("fromDate") String fromDate, @Query("toDate") String toDate);


    /**
     * 项目工程指标统计
     */
    @POST("/report/getDEBydepartmentGroupByProject")
    Call<JSONObject> getDByDepartmentProject(@Query("departmentCode") String departmentCode, @Query("fromDate") String fromDate, @Query("toDate") String toDate);

    /**
     * 周期工程指标
     */
    @POST("/report/getDEByMonth")
    Call<JSONObject> getDEByMonth(@Query("departmentCode") String departmentCode, @Query("fromDate") String fromDate, @Query("toDate") String toDate);


}
