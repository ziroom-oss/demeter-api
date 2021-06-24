package com.ziroom.tech.demeterapi.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ziroom.tech.demeterapi.common.api.CodeAnalysisApiEndPoint;
import com.ziroom.tech.demeterapi.common.utils.RetrofitCallAdaptor;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.CtoDevResp;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.DevOverviewStruct;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.DevStruct;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.EngineeringMetricResp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import retrofit2.Call;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author daijiankun
 */
@Component
@Slf4j
public class CodeAnalysisComponent {

    private final static String CODE_ATTRIBUTE = "code";

    private final static String DATA_ATTRIBUTE = "data";

    @Resource
    private CodeAnalysisApiEndPoint codeAnalysisApiEndPoint;

    @Resource
    private EhrComponent ehrComponent;

    public EngineeringMetricResp getDevelopmentEquivalent(String uid, Date fromDate, Date toDate) {

        // TODO: 2021/6/18 terrible code structure!
        UserDetailResp userDetail = ehrComponent.getUserDetail(uid);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String fromDateString = formatter.format(fromDate);
        String toDateString = formatter.format(toDate);

        Call<JSONObject> call = codeAnalysisApiEndPoint.getSingleDE(userDetail.getEmail(), fromDateString, toDateString);
        JSONObject response = RetrofitCallAdaptor.execute(call);

        EngineeringMetricResp resp = new EngineeringMetricResp();
        String success = "200";
        if (response.getString(CODE_ATTRIBUTE).equals(success)) {
            JSONObject jsonObject = response.getJSONObject(DATA_ATTRIBUTE);
            Integer insertions = jsonObject.getInteger("insertions");
            if (insertions != null) {
                resp.setInsertions(insertions);
            }
            Integer deletions = jsonObject.getInteger("deletions");
            if (deletions != null) {
                resp.setDeletions(deletions);
            }
            Integer devEquivalent = jsonObject.getInteger("devEquivalent");
            if (devEquivalent != null) {
                resp.setDevEquivalent(devEquivalent);
            }
            Integer commitCount = jsonObject.getInteger("commitCount");
            if (commitCount != null) {
                resp.setCommitCount(commitCount);
            }
        }
        return resp;
    }

    public CtoDevResp getDepartmentDe(String departmentCode, Date from, Date to) {
        CtoDevResp ctoDevResp = new CtoDevResp();
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

        Call<JSONObject> call2 =
                codeAnalysisApiEndPoint.getDEByDepartment(departmentCode, "", formatter.format(from), formatter.format(to));
        JSONObject response2 = RetrofitCallAdaptor.execute(call2);
        if (Objects.nonNull(response2)) {
            if (response2.getInteger("code").equals(200)) {
                Map map = (LinkedHashMap) response2.get("data");
                DevOverviewStruct devOverviewStruct = DevOverviewStruct.builder()
                        .insertions((Integer) map.get("insertions"))
                        .deletions((Integer) map.get("deletions"))
                        .devEquivalent((Integer) map.get("devEquivalent"))
                        .commitCounts((Integer) map.get("commitCount"))
                        .build();
                ctoDevResp.setDevOverviewStruct(devOverviewStruct);
            }
        }

        Call<JSONObject> call = codeAnalysisApiEndPoint.getDepartmentDe(departmentCode, formatter.format(from),
                formatter.format(to));
        JSONObject response = RetrofitCallAdaptor.execute(call);
        if (Objects.nonNull(response)) {
            if (response.getInteger("code").equals(200)) {
                JSONArray data = response.getJSONArray("data");
                List<DevStruct> devStructList = new ArrayList<>(16);
                for (int i = 0; i < data.size(); i++) {
                    Map map = (LinkedHashMap) data.get(i);
                    DevStruct struct = DevStruct.builder()
                            .name((String) map.get("departmentName"))
                            .devEquivalent((Integer) map.get("devEquivalent"))
                            .insertions((Integer) map.get("insertions"))
                            .build();
                    devStructList.add(struct);
                }
                ctoDevResp.setDepartmentDevList(devStructList);
            }
        }

        Call<JSONObject> call1 = codeAnalysisApiEndPoint
                .getDByDepartmentProject(departmentCode, formatter.format(from), formatter.format(to));
        JSONObject response1 = RetrofitCallAdaptor.execute(call1);
        if (Objects.nonNull(response1)) {
            if (response1.getInteger("code").equals(200)) {
                JSONArray data = response1.getJSONArray("data");
                List<DevStruct> devStructList = new ArrayList<>(16);
                for (Object datum : data) {
                    Map<String, Object> map = (LinkedHashMap) datum;
                    DevStruct struct = DevStruct.builder()
                            .name((String) map.get("projectName"))
                            .devEquivalent((Integer) map.get("devEquivalent"))
                            .insertions((Integer) map.get("insertions"))
                            .build();
                    devStructList.add(struct);
                }
                ctoDevResp.setProjectDevList(devStructList);
            }
        }

        Call<JSONObject> call3 =
                codeAnalysisApiEndPoint.getDEByMonth(departmentCode, formatter.format(from), formatter.format(to));
        JSONObject response3 = RetrofitCallAdaptor.execute(call3);
        if (Objects.nonNull(response3)) {
            if (response1.getInteger("code").equals(200)) {
                List<LinkedHashMap> list = (ArrayList) response3.get("data");
                List<DevStruct> periodList = new ArrayList<>(16);
                list.forEach(item -> {
                    DevStruct devStruct = DevStruct.builder()
                            .name((String) item.get("month"))
                            .insertions((Integer) item.get("insertions"))
                            .devEquivalent((Integer) item.get("devEquivalent"))
                            .build();
                    periodList.add(devStruct);
                });
                ctoDevResp.setPeriodList(periodList);
            }
        }

        return ctoDevResp;
    }
}
