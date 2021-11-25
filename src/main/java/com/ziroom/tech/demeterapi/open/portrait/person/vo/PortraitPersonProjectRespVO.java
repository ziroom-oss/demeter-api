package com.ziroom.tech.demeterapi.open.portrait.person.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xuzeyu
 */
@Data
@NoArgsConstructor
public class PortraitPersonProjectRespVO {
    /**
     * 项目名字
     */
    private String name;
    /**
     * 开发当量
     */
    private Long value;
}
