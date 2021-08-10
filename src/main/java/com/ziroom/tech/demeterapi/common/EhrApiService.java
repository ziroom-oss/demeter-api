package com.ziroom.tech.demeterapi.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ziroom.tech.demeterapi.common.api.EhrApiEndPoint;
import com.ziroom.tech.demeterapi.common.utils.RetrofitCallAdaptor;
import com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.req.EhrApiEmpListReq;
import com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.req.EhrApiSimpleReq;
import com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.resp.EhrApiResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.resp.EhrApiSimpleResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.resp.EhrDeptResp;
import com.ziroom.tech.demeterapi.po.dto.resp.ehrapi.resp.EhrEmpUserResp;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import retrofit2.Call;

/**
 * @author chenx34
 * @date 2020/4/23 13:05
 */
@Slf4j
@Service
public class EhrApiService {
    /**
     * ehr响应中的错误信息属性名
     */
    private final static String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";
    /**
     * ehr响应中的错误码属性名
     */
    private final static String ERROR_CODE_ATTRIBUTE = "errorCode";
    /**
     * ehr响应中的数据属性名
     */
    private final static String DATA_ATTRIBUTE = "data";

    private final static String CODE_ATTRIBUTE = "code";

    private final static String SUCCESS_CODE = "20000";
    /**
     * ehr响应中的状态属性名
     */
    private final static String STATUS_ATTRIBUTE = "status";
    private final static String DEFAULT_SETID = "101";

    @Resource
    private EhrApiEndPoint ehrApiEndPoint;

    /**
     * 查询所有人员
     *
     * @return
     */
    public List<EhrEmpUserResp> queryAllUser(EhrApiEmpListReq ehrApiEmpListReq) {
        List<EhrEmpUserResp> result = Lists.newArrayList();
        Map<String, Object> stringObjectMap = initReqMap(ehrApiEmpListReq);
        Call<JSONObject> jsonObjectCall = ehrApiEndPoint.empList(stringObjectMap);
        JSONObject resp = RetrofitCallAdaptor.execute(jsonObjectCall);
        if (Objects.equals(resp.getString("com/ziroom/message"), "success")) {
            JSONObject data = resp.getJSONObject("data");
            Integer totalPages = data.getInteger("totalPages");
            JSONArray list = data.getJSONArray("list");
            result.addAll(list.stream().map(o -> JSON.parseObject(JSON.toJSONString(o))).map(EhrEmpUserResp::copyByJson).collect(Collectors.toList()));
            for (int i = 2; i < totalPages; i++) {
                ehrApiEmpListReq.setPageIndex(i);
                Call<JSONObject> respTemp = ehrApiEndPoint.empList(initReqMap(ehrApiEmpListReq));
                if (Objects.equals(resp.getString("com/ziroom/message"), "success")) {
                    JSONArray tempList = data.getJSONArray("list");
                    result.addAll(tempList.stream().map(o -> JSON.parseObject(JSON.toJSONString(o))).map(EhrEmpUserResp::copyByJson).collect(Collectors.toList()));
                }
            }
        }
        return result;
    }

    public EhrApiSimpleResp getEmpSimple(EhrApiSimpleReq ehrApiSimpleReq) {
        Map<String, Object> stringObjectMap = initReqMap(ehrApiSimpleReq);
        Call<EhrApiResp<EhrApiSimpleResp>> call = ehrApiEndPoint.getEmpSimple(stringObjectMap);
        EhrApiResp<EhrApiSimpleResp> resp = RetrofitCallAdaptor.execute(call);
        return resp.getData();
    }

    /**
     * 批量查询部门
     *
     * @param deptIds deptIds
     * @return 结果
     */
    public List<EhrDeptResp> queryBatchCodes(Collection<String> deptIds) {
        List<EhrDeptResp> list = Lists.newArrayList();
        Lists.partition(Arrays.asList(deptIds.toArray()), 100).parallelStream().map(strings -> Strings.join(strings,
                ',')).forEach(s -> {
            Call<JSONObject> byOrgCodes = ehrApiEndPoint.findByOrgCodes(s);
            JSONObject execute = RetrofitCallAdaptor.execute(byOrgCodes);
            if (Objects.equals(execute.getString("com/ziroom/message"), "success")) {
                execute.getJSONArray("data").forEach(o -> {
                    Map jsonObject = (Map) o;
                    EhrDeptResp ehrDeptResp = new EhrDeptResp();
                    ehrDeptResp.setCode(jsonObject.get("orgCode").toString());
                    ehrDeptResp.setName(jsonObject.get("orgName").toString());
                    list.add(ehrDeptResp);
                });
            }
        });
        return list;
    }

    private Map<String, Object> initReqMap(Object o) {
        Map<String, Object> result = Maps.newHashMap();
        FieldUtils.getAllFieldsList(o.getClass()).forEach(field -> {
            try {
                Object value = FieldUtils.readDeclaredField(o, field.getName(), true);
                if (Objects.nonNull(value)) {
                    result.put(field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                // ignore
            }
        });
        return result;
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            double temp = 0.5 * 0.23 + 0.2 * 0.1 + 0.3 * 0.99;
            System.out.println(temp);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        log.error("are you ok {} {} ", "heheh", "hahah", new RuntimeException());
    }
}
