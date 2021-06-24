package com.ziroom.tech.demeterapi.common.api;

import com.alibaba.fastjson.JSONObject;
import com.ziroom.tech.boot.annotation.RetrofitService;
import com.ziroom.tech.demeterapi.po.dto.req.worktop.CtoPerspectiveReq;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * @author daijiankun
 */
@RetrofitService("worktop")
public interface WorktopApiEndPoint {


    /**
     * 开发类
     * @param request
     * @return
     */
    @POST("/api/view/cto/overview")
    Call<JSONObject> getWorktopOverview(@Body CtoPerspectiveReq request, @Header("uid") String uid);
}
