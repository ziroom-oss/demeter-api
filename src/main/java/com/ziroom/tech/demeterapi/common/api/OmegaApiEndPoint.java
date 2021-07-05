package com.ziroom.tech.demeterapi.common.api;

import com.alibaba.fastjson.JSONObject;
import com.ziroom.tech.boot.annotation.RetrofitService;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author daijiankun
 */
@RetrofitService("omega")
public interface OmegaApiEndPoint {


    @GET("/api/out/getDeployNorm")
    Call<JSONObject> getDeployNorm(@Query("deptId") String deptId, @Query("start") String start, @Query("end") String end);

    @GET("/api/out/getPersonaltNorm")
    Call<JSONObject> getPersonalNorm(@Query("userEmail") String userEmail, @Query("start") String start, @Query("end") String end);
}
