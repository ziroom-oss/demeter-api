package com.ziroom.tech.demeterapi.po.dto.resp.halo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author xuzeyu
 **/
@Data
public class AuthResp {

    private List<String> roles = Lists.newArrayList();
}
