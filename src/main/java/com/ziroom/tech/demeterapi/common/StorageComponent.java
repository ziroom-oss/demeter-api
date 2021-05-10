package com.ziroom.tech.demeterapi.common;

import com.alibaba.fastjson.JSONObject;
import com.ziroom.tech.demeterapi.common.api.StorageApiEndPoint;
import com.ziroom.tech.demeterapi.common.utils.RetrofitCallAdaptor;
import com.ziroom.tech.demeterapi.po.dto.req.storage.QueryParam;
import com.ziroom.tech.demeterapi.po.dto.req.storage.UploadParam;
import com.ziroom.tech.demeterapi.po.dto.resp.storage.DownloadResp;
import com.ziroom.tech.demeterapi.po.dto.resp.storage.UploadResp;
import org.springframework.stereotype.Component;
import retrofit2.Call;

import javax.annotation.Resource;

/**
 * @author daijiankun
 */
@Component
public class StorageComponent {

    @Resource
    private StorageApiEndPoint storageApiEndPoint;

    public UploadResp uploadFile(UploadParam uploadParam) {
        Call<UploadResp> call = storageApiEndPoint.uploadFile(uploadParam);
        UploadResp response = RetrofitCallAdaptor.execute(call);
        return response;
    }

    public DownloadResp downloadFile(String uuidString) {
        QueryParam queryParam = new QueryParam();
        queryParam.setSource("");
        queryParam.setUuid(uuidString);
        Call<DownloadResp> call = storageApiEndPoint.queryFile(queryParam);
        return RetrofitCallAdaptor.execute(call);
    }
}
