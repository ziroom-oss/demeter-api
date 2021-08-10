package com.ziroom.tech.demeterapi.common.api;

import com.ziroom.tech.boot.annotation.RetrofitService;
import com.ziroom.tech.demeterapi.po.dto.req.ranking.RankingReq;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.AnalysisReq;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.AnalysisResp;
import com.ziroom.tech.demeterapi.po.dto.resp.flink.Resp;
import java.util.List;

import com.ziroom.tech.demeterapi.po.dto.resp.rankings.InfoRanking;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankingInfo;
import com.ziroom.tech.demeterapi.po.dto.resp.rankings.RankingResp;
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
     * 個人开发当量查询
     * @param analysisReq request
     * @return response
     */
    @POST("/api/v1/data/staffDevEquivalentSort")
    Call<Resp<List<InfoRanking>>> individualdevEquivsearch(@Body AnalysisReq analysisReq);
    /**
     * 员工个人开发当量排行
     * @param analysisReq
     * @return
     */
    @POST("/api/v1/data/staffDevEquivalentCount")
    Call<Resp<Integer>> individevEquivsort(@Body AnalysisReq analysisReq);
    /**
     * 個人开发价值查询
     * @param analysisReq request
     * @return response
     * /api/v1/data/staffDevelopmentValueSort
     */
    @POST("/api/v1/data/staffDevelopmentValueSort")
    Call<Resp<List<InfoRanking>>> individualdevEvaluesearch(@Body AnalysisReq analysisReq);
    /**
     * 员工个人开发价值排行
     * @param analysisReq
     * @return
     * /api/v1/data/staffDevelopmentValueSort
     */
    @POST("/api/v1/data/staffDevelopmentValueCount")
    Call<Resp<Integer>> individevEvaluesort(@Body AnalysisReq analysisReq);
    /**
     * 個人开发质量查询
     * @param analysisReq request
     * @return response
     */
    @POST("/api/v1/data/staffDevQualitySort")
    Call<Resp<List<InfoRanking>>> individualdevQualitysearch(@Body AnalysisReq analysisReq);
    /**
     * 员工个人开发质量排行
     * @param analysisReq
     * @return
     */
    @POST("/api/v1/data/staffDevelopmentValueCount")
    Call<Resp<Integer>> individevQualitysort(@Body AnalysisReq analysisReq);
    /**
     * 個人开发效率查询
     * @param analysisReq request
     * @return response
     */
    @POST("/api/v1/data/staffDevEfficiencySort")
    Call<Resp<List<InfoRanking>>> individualdevEfficiencysearch(@Body AnalysisReq analysisReq);
    /**
     * 员工个人开发效率排行
     * @param analysisReq
     * @return
     */
    @POST("/api/v1/data/staffDevelopmentValueCount")
    Call<Resp<Integer>> individevEfficiencysort(@Body AnalysisReq analysisReq);


    /**
     * 部門开发当量查询
     * @param analysisReq request
     * @return response
     */
    @POST("/api/v1/data/departmentDevEquivalentSort")
    Call<Resp<List<InfoRanking>>> deptdevEquivsearch(@Body AnalysisReq analysisReq);
    /**
     * 部門开发当量排行
     * @param analysisReq
     * @return
     */
    @POST("/api/v1/data/departmentDevEquivalentSort")
    Call<Resp<Integer>> deptdevEquivsort(@Body AnalysisReq analysisReq);
    /**
     * 部門开发价值查询
     * @param analysisReq request
     * @return response
     * /api/v1/data/staffDevelopmentValueSort
     */
    @POST("/api/v1/data/staffDevelopmentValueSort")
    Call<Resp<List<InfoRanking>>> deptdevEvaluesearch(@Body AnalysisReq analysisReq);
    /**
     * 部門开发价值排行
     * @param analysisReq
     * @return
     * /api/v1/data/staffDevelopmentValueSort
     */
    @POST("/api/v1/data/staffDevelopmentValueCount")
    Call<Resp<Integer>> deptdevEvaluesort(@Body AnalysisReq analysisReq);
    /**
     * 部門开发质量查询
     * @param analysisReq request
     * @return response
     */
    @POST("/api/v1/data/staffDevQualitySort")
    Call<Resp<List<InfoRanking>>> deptdevQualitysearch(@Body AnalysisReq analysisReq);
    /**
     * 部門开发质量排行
     * @param analysisReq
     * @return
     */
    @POST("/api/v1/data/staffDevelopmentValueCount")
    Call<Resp<Integer>> deptdevQualitysort(@Body AnalysisReq analysisReq);
    /**
     * 部門开发效率查询
     * @param analysisReq request
     * @return response
     */
    @POST("/api/v1/data/staffDevEfficiencySort")
    Call<Resp<List<InfoRanking>>> deptdevEfficiencysearch(@Body AnalysisReq analysisReq);
    /**
     * 部門开发效率排行
     * @param analysisReq
     * @return
     */
    @POST("/api/v1/data/staffDevelopmentValueCount")
    Call<Resp<Integer>> deptEfficiencysort(@Body AnalysisReq analysisReq);


}
