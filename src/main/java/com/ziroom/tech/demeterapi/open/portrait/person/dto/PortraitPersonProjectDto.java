package com.ziroom.tech.demeterapi.open.portrait.person.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xuzeyu
 */
@Data
@NoArgsConstructor
public class PortraitPersonProjectDto {
    /**
     * 项目名字
     */
    private String projectName;
    /**
     * 开发当量
     */
    private Long devEquivalent;
}
