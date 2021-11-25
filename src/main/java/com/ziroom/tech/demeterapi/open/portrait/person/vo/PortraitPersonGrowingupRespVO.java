package com.ziroom.tech.demeterapi.open.portrait.person.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xuzeyu
 */
@Data
@NoArgsConstructor
public class PortraitPersonGrowingupRespVO {
    /**
     * 标题
     */
    private String title;

    /**
     * 样式类型
     */
    private Integer type;

    /**
     * 指标
     */
    private List<Point> points;

    @Data
    @NoArgsConstructor
    public static class Point{
        /**
         * 指标名称
         */
        private String coreName;

        /**
         * 指标值
         */
        private String coreData;
    }

}
