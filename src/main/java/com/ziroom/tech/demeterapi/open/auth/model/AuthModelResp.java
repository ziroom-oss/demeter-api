package com.ziroom.tech.demeterapi.open.auth.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author xuzeyu
 **/
@Data
public class AuthModelResp {

    /**
     * 角色
     */
    private List<String> roles = Lists.newArrayList();
}
