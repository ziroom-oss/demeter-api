package com.ziroom.tech.demeterapi.open.portrait.person.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xuzeyu
 */
@Data
@NoArgsConstructor
public class PortraitPersonSkillDto {
    /**
     * 认证技能数量
     */
    private Integer skillNum = 0;
    /**
     * 认证技能点数量
     */
    private Integer skillPointNum = 0;
}
