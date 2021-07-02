package com.ziroom.tech.demeterapi.common.utils;

import com.ziroom.tech.demeterapi.common.enums.ErrorCode;
import com.ziroom.tech.demeterapi.common.exception.BusinessException;
import lombok.experimental.UtilityClass;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;


/**
 * @author daijiankun
 */
@UtilityClass
public class RetrofitCallAdaptor {


    public static <T> T execute(Call<T> retrofitCall) {
        try {
            Response<T> response = retrofitCall.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            ResponseBody errorBody = response.errorBody();
            throw new BusinessException(errorBody == null ? "请求失败" : errorBody.string(), ErrorCode.NET_REQUEST_ERROR);
        } catch (IOException e) {
            throw new BusinessException(e, ErrorCode.NET_REQUEST_SUSPEND);
        }
    }
}
