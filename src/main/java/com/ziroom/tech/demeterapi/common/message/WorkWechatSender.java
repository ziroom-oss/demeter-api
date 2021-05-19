package com.ziroom.tech.demeterapi.common.message;

import com.alibaba.fastjson.JSON;
import com.ziroom.tech.demeterapi.utils.DateUtils;
import com.ziroom.tech.demeterapi.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author liangrk
 * @date 2021/3/19
 */
@Component("WECHAT")
@Slf4j
public class WorkWechatSender implements MessageSender {

    @Value("${message.baseUrl:http://message.t.ziroom.com/}")
    private String baseUrl;

    @Value("${message.model.workWechat}")
    private String workWechatModel;

    @Value("${message.token.workWechat}")
    private String workWechatToken;

    @Override
    public void send(String content, List<String> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            log.error("发送消息失败, userList为空");
            return;
        }

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> p =  new HashMap<>();
        p.put("title","360员工成长平台消息通知");
        p.put("description", "<br><div class=\"normal\">" + content + "</div><br><br><div class=\"gray\">"+ DateUtils.dateToStr(new Date(),"yyyy年MM月dd日")+"</div>");
        p.put("url","http://demeter-ui.kp.ziroom.com");
        p.put("btntxt","详情");
        params.put("params", p);
        params.put("modelCode",workWechatModel);

        params.put("token", workWechatToken);
        // 应用消息
        params.put("pushType", "app");
        // 文本消息
        params.put("msgType", "textcard");
        params.put("toUser", String.join("|", userList));

        String uri = baseUrl + "/api/work/wechat/send";
        String paramJson = JSON.toJSONString(params);
        log.info("发送消息请求入参：{}", paramJson);
        String retMsg = HttpUtil.postRequest(uri, paramJson, null);
        log.info("发送消息请求返回：{}", retMsg);
    }

}
