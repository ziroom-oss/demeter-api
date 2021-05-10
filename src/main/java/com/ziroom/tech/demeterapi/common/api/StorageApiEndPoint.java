package com.ziroom.tech.demeterapi.common.api;

import com.alibaba.fastjson.JSONObject;
import com.ziroom.tech.boot.annotation.RetrofitService;
import com.ziroom.tech.demeterapi.po.dto.req.storage.DeleteParam;
import com.ziroom.tech.demeterapi.po.dto.req.storage.QueryParam;
import com.ziroom.tech.demeterapi.po.dto.req.storage.UploadParam;
import com.ziroom.tech.demeterapi.po.dto.resp.storage.DownloadResp;
import com.ziroom.tech.demeterapi.po.dto.resp.storage.UploadResp;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author daijiankun
 */
@RetrofitService("storage")
public interface StorageApiEndPoint {

    /**
     * 上传文件
     * @param uploadParam 上传文件请求体
     * @return Call<JSONObject>
     */
    @POST("/api/file/upload")
    Call<UploadResp> uploadFile(@Body UploadParam uploadParam);


    /**
     * 删除文件
     * @param deleteParam 删除文件请求体
     * @return
     */
    @POST("/api/file/delete")
    Call<JSONObject> deleteFile(@Body DeleteParam deleteParam);


    /**
     * 查询文件
     * @param queryParam 查询文件请求体
     * @return
     */
    @POST("/api/file/info")
    Call<DownloadResp> queryFile(@Body QueryParam queryParam);
}
