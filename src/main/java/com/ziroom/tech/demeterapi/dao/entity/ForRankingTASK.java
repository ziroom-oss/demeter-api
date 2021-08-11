package com.ziroom.tech.demeterapi.dao.entity;


import lombok.Builder;
import lombok.Data;

/**
 * @zhangxt3
 */
@Builder
@Data
public class ForRankingTASK {

   private Integer taskId;
    //统计总数
    private Integer sumAll;
}
