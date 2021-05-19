package com.ziroom.tech.demeterapi.po.dto.resp.halo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author huangqiaowei
 * @date 2021-02-02 16:00
 **/
@Data
public class AuthResp {

    private Map<String, String> functions = Maps.newHashMap();

    private List<String> menulist = Lists.newArrayList();

    private List<String> roles = Lists.newArrayList();
}
