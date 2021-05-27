package com.ziroom.tech.demeterapi.common.api;

import com.ziroom.tech.boot.annotation.RetrofitService;
import com.ziroom.tech.demeterapi.po.dto.resp.storage.UploadResp;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author daijiankun
 */
@RetrofitService("ca")
public interface CodeAnalysisApiEndPoint {


    @GET("/report/getDE")
    Call<JSONObject> getDevelopmentEquivalent(@Query("account") String email, @Query("fromDate") String fromDate, @Query("toDate") String toDate);
}
