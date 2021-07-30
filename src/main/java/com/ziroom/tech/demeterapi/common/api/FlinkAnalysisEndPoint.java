package com.ziroom.tech.demeterapi.common.api;

import com.ziroom.tech.boot.annotation.RetrofitService;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.AnalysisReq;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.AnalysisResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.Resp;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author daijiankun
 */
@RetrofitService("flink-analysis")
public interface FlinkAnalysisEndPoint {

    /**
     * 查询分析结果
     * @param analysisReq request
     * @return response
     */
    @POST("/api/v1/data/search")
    Call<Resp<List<AnalysisResp>>> getAnalysisResp(@Body AnalysisReq analysisReq);
}
