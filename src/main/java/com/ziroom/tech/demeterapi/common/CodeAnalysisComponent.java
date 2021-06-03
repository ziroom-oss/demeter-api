package com.ziroom.tech.demeterapi.common;

import com.alibaba.fastjson.JSONObject;
import com.ziroom.tech.demeterapi.common.api.CodeAnalysisApiEndPoint;
import com.ziroom.tech.demeterapi.common.utils.RetrofitCallAdaptor;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.EngineeringMetricResp;
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

        UserDetailResp userDetail = ehrComponent.getUserDetail(uid);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String fromDateString = formatter.format(fromDate);
        String toDateString = formatter.format(toDate);

        Call<JSONObject> call = codeAnalysisApiEndPoint.getDevelopmentEquivalent(userDetail.getEmail(), fromDateString, toDateString);
        JSONObject response = RetrofitCallAdaptor.execute(call);

        String success = "200";
        if (response.getString(CODE_ATTRIBUTE).equals(success)) {
            JSONObject jsonObject = response.getJSONObject(DATA_ATTRIBUTE);
            EngineeringMetricResp resp = new EngineeringMetricResp();
            Integer insertions = jsonObject.getInteger("insertions");
            if (insertions != null) {
                resp.setInsertions(insertions);
            }
            Integer deletions = jsonObject.getInteger("deletions");
            if (deletions != null) {
                resp.setDeletions(deletions);
            }
            Integer devEquivalent = jsonObject.getInteger("dev_equivalent");
            if (devEquivalent != null) {
                resp.setDevEquivalent(devEquivalent);
            }
            Integer commitCount = jsonObject.getInteger("commit_count");
            if (commitCount != null) {
                resp.setCommitCount(commitCount);
            }
            return resp;
        }
        return null;
    }
}
