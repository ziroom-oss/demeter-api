package com.ziroom.tech.demeterapi.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ziroom.tech.demeterapi.common.api.CodeAnalysisApiEndPoint;
import com.ziroom.tech.demeterapi.common.api.OmegaApiEndPoint;
import com.ziroom.tech.demeterapi.common.utils.RetrofitCallAdaptor;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.CtoDevResp;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.DevOverviewStruct;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.DevStruct;
import com.ziroom.tech.demeterapi.po.dto.resp.portrait.EngineeringMetricResp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import retrofit2.Call;

/**
 * @author daijiankun
 */
@Component
@Slf4j
public class OmegaComponent {

    private final static String CODE_ATTRIBUTE = "code";

    private final static String DATA_ATTRIBUTE = "data";

    @Resource
    private OmegaApiEndPoint omegaApiEndPoint;

    @Resource
    private EhrComponent ehrComponent;

    public void getDeployNorm(String uid, Date fromDate, Date toDate) {

        // TODO: 2021/6/18 terrible code structure!
        UserDetailResp userDetail = ehrComponent.getUserDetail(uid);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String fromDateString = formatter.format(fromDate);
        String toDateString = formatter.format(toDate);

        Call<JSONObject> call = omegaApiEndPoint.getDeployNorm(userDetail.getEmail(), fromDateString, toDateString);
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
        }
    }
}
