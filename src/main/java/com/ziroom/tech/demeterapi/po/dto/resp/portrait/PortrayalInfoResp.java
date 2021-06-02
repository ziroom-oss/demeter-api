package com.ziroom.tech.demeterapi.po.dto.resp.portrait;

import lombok.Data;

import java.util.List;

/**
 * @author daijiankun
 */
@Data
public class PortrayalInfoResp {


    /**
     * 基本信息
     */
    private UserInfo userInfo;

    /**
     * 员工成长信息
     */
    private GrowthInfo growthInfo;

    /**
     * 雷达图
     */
    private List<RadarGraph> radarGraphs;
}
