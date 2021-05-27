package com.ziroom.tech.demeterapi.common;

import com.alibaba.fastjson.JSONObject;
import com.ziroom.tech.demeterapi.common.api.StorageApiEndPoint;
import com.ziroom.tech.demeterapi.common.utils.RetrofitCallAdaptor;
import com.ziroom.tech.demeterapi.po.dto.req.storage.QueryParam;
import com.ziroom.tech.demeterapi.po.dto.req.storage.UploadParam;
import com.ziroom.tech.demeterapi.po.dto.resp.storage.DownloadResp;
import com.ziroom.tech.demeterapi.po.dto.resp.storage.UploadResp;
import com.ziroom.tech.demeterapi.po.dto.resp.storage.ZiroomFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Call;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author daijiankun
 */
@Component
@Slf4j
public class StorageComponent {

    @Resource
    private StorageApiEndPoint storageApiEndPoint;

    public ZiroomFile uploadFile(MultipartFile multipartFile) {
        String fileBase64String = null;
        try {
            fileBase64String = Base64Utils.encodeToString(multipartFile.getBytes());
        } catch (IOException e) {
            log.error("id:{} use base64 encode file has happened error", e.getMessage());
        }
        UploadParam uploadParam = UploadParam.builder()
                .source("ceph.tech.demeter")
                .filename(multipartFile.getOriginalFilename())
                .base64(fileBase64String)
                .type("if")
                .build();
        Call<UploadResp> call = storageApiEndPoint.uploadFile(uploadParam);
        UploadResp response = RetrofitCallAdaptor.execute(call);
        return response.getFile();
    }

    public DownloadResp downloadFile(String uuidString) {
        QueryParam queryParam = new QueryParam();
        queryParam.setSource("ceph.tech.demeter");
        queryParam.setUuid(uuidString);
        Call<DownloadResp> call = storageApiEndPoint.queryFile(queryParam);
        return RetrofitCallAdaptor.execute(call);
    }
}
