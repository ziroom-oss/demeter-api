package com.ziroom.tech.demeterapi.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ziroom.tech.demeterapi.common.api.WorktopApiEndPoint;
import com.ziroom.tech.demeterapi.common.utils.RetrofitCallAdaptor;
import com.ziroom.tech.demeterapi.po.dto.req.worktop.CtoPerspectiveReq;
import java.util.Date;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import retrofit2.Call;

/**
 * @author daijiankun
 */
@Component
@Slf4j
public class WorktopComponent {

    private final static String CODE_ATTRIBUTE = "code";

    private final static String DATA_ATTRIBUTE = "data";

    @Resource
    private WorktopApiEndPoint worktopApiEndPoint;

    @Resource
    private EhrComponent ehrComponent;

    @Cacheable(value = "caffeine", key = "#root.methodName + #root.args[0]")
    public JSONArray getWorktopOverview(String deptId, Date fromDate, Date toDate) {

        CtoPerspectiveReq requestBody = new CtoPerspectiveReq();
        requestBody.setDeptId(deptId);
        requestBody.setBegin(fromDate);
        requestBody.setEnd(toDate);
        Call<JSONObject> call = worktopApiEndPoint.getWorktopOverview(requestBody, OperatorContext.getOperator());
        JSONObject response = RetrofitCallAdaptor.execute(call);
        String success = "200";
        if (response.getString(CODE_ATTRIBUTE).equals(success)) {
            return response.getJSONArray(DATA_ATTRIBUTE);
        }
        return new JSONArray();
    }

    @Cacheable(value = "caffeine", key = "#root.methodName + #root.args[0]")
    public JSONArray getDepartmentData(String deptId, Date fromDate, Date toDate) {
        CtoPerspectiveReq requestBody = new CtoPerspectiveReq();
        requestBody.setDeptId(deptId);
        requestBody.setBegin(fromDate);
        requestBody.setEnd(toDate);
        Call<JSONObject> call = worktopApiEndPoint.getDepartmentData(requestBody, OperatorContext.getOperator());
        JSONObject response = RetrofitCallAdaptor.execute(call);
        String success = "200";
        if (response.getString(CODE_ATTRIBUTE).equals(success)) {
            return response.getJSONArray(DATA_ATTRIBUTE);
        }
        return new JSONArray();
    }

    @Cacheable(value = "caffeine", key = "#root.methodName + #root.args[0]")
    public JSONArray getProjectData(String deptId, Date fromDate, Date toDate) {
        CtoPerspectiveReq requestBody = new CtoPerspectiveReq();
        requestBody.setDeptId(deptId);
        requestBody.setBegin(fromDate);
        requestBody.setEnd(toDate);
        Call<JSONObject> call = worktopApiEndPoint.getProjectData(requestBody, OperatorContext.getOperator());
        JSONObject response = RetrofitCallAdaptor.execute(call);
        String success = "200";
        if (response.getString(CODE_ATTRIBUTE).equals(success)) {
            return response.getJSONArray(DATA_ATTRIBUTE);
        }
        return new JSONArray();
    }

    @Cacheable(value = "caffeine", key = "#root.methodName + #root.args[0]")
    public JSONArray getMonthData(String deptId, Date fromDate, Date toDate) {
        CtoPerspectiveReq requestBody = new CtoPerspectiveReq();
        requestBody.setDeptId(deptId);
        requestBody.setBegin(fromDate);
        requestBody.setEnd(toDate);
        Call<JSONObject> call = worktopApiEndPoint.getMonthData(requestBody, OperatorContext.getOperator());
        JSONObject response = RetrofitCallAdaptor.execute(call);
        String success = "200";
        if (response.getString(CODE_ATTRIBUTE).equals(success)) {
            return response.getJSONArray(DATA_ATTRIBUTE);
        }
        return new JSONArray();
    }

    @Cacheable(value = "caffeine", key = "#root.methodName + #root.args[0]")
    public JSONArray getLevelData(String deptId, Date fromDate, Date toDate) {
        CtoPerspectiveReq requestBody = new CtoPerspectiveReq();
        requestBody.setDeptId(deptId);
        requestBody.setBegin(fromDate);
        requestBody.setEnd(toDate);
        Call<JSONObject> call = worktopApiEndPoint.getLevelData(requestBody, OperatorContext.getOperator());
        JSONObject response = RetrofitCallAdaptor.execute(call);
        String success = "200";
        if (response.getString(CODE_ATTRIBUTE).equals(success)) {
            return response.getJSONArray(DATA_ATTRIBUTE);
        }
        return new JSONArray();
    }
}
