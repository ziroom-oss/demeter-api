package com.ziroom.tech.demeterapi.utils;

import java.util.UUID;

/**
 * @author libingsi
 * @date 2021/6/22 10:59
 */
public class UUIDUtil {

    /**
     * 获取id
     *
     * @return 32位uuid
     */
    public static String getId() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

}
