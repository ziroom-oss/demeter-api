package com.ziroom.tech.demeterapi.common;

import com.ziroom.tech.demeterapi.common.api.CodeAnalysisApiEndPoint;
import com.ziroom.tech.demeterapi.common.utils.RetrofitCallAdaptor;
import com.ziroom.tech.demeterapi.po.dto.resp.ehr.UserDetailResp;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
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

    @Resource
    private CodeAnalysisApiEndPoint codeAnalysisApiEndPoint;

    @Resource
    private EhrComponent ehrComponent;

    public void getDevelopmentEquivalent(String uid, Date fromDate, Date toDate) {

        UserDetailResp userDetail = ehrComponent.getUserDetail(uid);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String fromDateString = formatter.format(fromDate);
        String toDateString = formatter.format(toDate);

        Call<JSONObject> call = codeAnalysisApiEndPoint.getDevelopmentEquivalent(userDetail.getEmail(), fromDateString, toDateString);

        JSONObject response = RetrofitCallAdaptor.execute(call);
    }
}
