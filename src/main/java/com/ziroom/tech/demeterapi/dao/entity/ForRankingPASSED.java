package com.ziroom.tech.demeterapi.dao.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @zhangxt3
 */
@Builder
@Data
public class ForRankingPASSED {
    //用户或技能id
    private String receiverUid;

//    private Long parentId;
//    private Integer taskId;
    //统计总数
    private Integer sumAll;
}
