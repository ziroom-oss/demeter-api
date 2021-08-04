package com.ziroom.tech.demeterapi.common.api;

import com.ziroom.tech.boot.annotation.RetrofitService;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.AnalysisReq;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.AnalysisResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.CostReq;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.CostResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.EfficientResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.QualityReq;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.QualityResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.Resp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.StabilityReq;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.StabilityResp;
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

    /**
     * 获取部门鲁棒性，稳定性数据
     * @param stabilityReq
     * @return
     */
    @POST("/api/v1/data/getDeptEfficiencyData")
    Call<Resp<List<StabilityResp>>> getStabilityData(@Body StabilityReq stabilityReq);


    /**
     * 获取质量类数据
     * @param qualityReq
     * @return
     */
    @POST("/api/v1/data/getQualityByDept")
    Call<Resp<QualityResp>> getQualityData(@Body QualityReq qualityReq);

    /**
     * @param costReq
     * @return
     */
    @POST("/api/v1/data/getEhrTotalUserByDept")
    Call<Resp<CostResp>> getCostData(@Body CostReq costReq);

    /**
     * 获取部门效率类数据
     * @param analysisReq
     * @return
     */
    @POST("/api/v1/data/getEfficiencyByDept")
    Call<Resp<EfficientResp>> getEfficientData(@Body AnalysisReq analysisReq);
}
