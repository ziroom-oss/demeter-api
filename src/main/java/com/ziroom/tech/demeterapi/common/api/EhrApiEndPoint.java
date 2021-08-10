package com.ziroom.tech.demeterapi.common.api;

import com.alibaba.fastjson.JSONObject;
import com.ziroom.tech.boot.annotation.RetrofitService;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * @author daijiankun
 * @author chenx34
 */
@RetrofitService("ehrapi")
public interface EhrApiEndPoint {

    /**
     * 根据条件查询员工基本信息列表
     * <p>
     * 可以查全国所有员工
     * <p>
     * swagger 地址： http://ehr-api.t.ziroom.com/swagger-ui.html
     */
    @GET("/emps/list")
    Call<JSONObject> empList(@QueryMap Map<String, Object> empListReqMap);


    /**
     * 批量查询部门
     * <p>
     * swagger 地址： http://ehr-api.t.ziroom.com/swagger-ui.html
     */
    @GET("/org/find/findByOrgCodes")
    Call<JSONObject> findByOrgCodes(@Query("orgCodes") String orgCodes);


    @GET("/emps/simple")
    Call<JSONObject> getEmpSimple(@QueryMap Map<String, Object> empSimpleReqMap);
}
