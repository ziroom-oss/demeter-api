package com.ziroom.tech.demeterapi.po.dto.resp.monthRept;

import com.ziroom.tech.demeterapi.dao.entity.DemeterCoreData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DemeterCoreDataResp {

    @ApiModelProperty(value = "同比率")
    private String tbRate;

    @ApiModelProperty(value = "环比率")
    private String hbRate;


    @ApiModelProperty(value = "同比")
    private BigDecimal tb;

    @ApiModelProperty(value = "环比")
    private BigDecimal hb;

    @ApiModelProperty(value = "当前")
    private BigDecimal cur;


    @ApiModelProperty(value = "曲线提供的数据")
    private List<String> foldLine;

    @ApiModelProperty(value = "当前数据")
    private DemeterCoreData demeterCoreDataCurrent;


    @ApiModelProperty(value = "同比数据")
    private DemeterCoreData demeterCoreDataLastMonth;


    @ApiModelProperty(value = "环比数据")
    private DemeterCoreData demeterCoreDataLastYear;

    public DemeterCoreDataResp() {

    }

    public DemeterCoreDataResp(DemeterCoreData demeterCoreDataCurrent,
                               DemeterCoreData demeterCoreDataLastMonth,
                               DemeterCoreData demeterCoreDataLastYear,
                               String tbRate, String hbRate,
                               List<String> foldLine) {
        this.demeterCoreDataCurrent = demeterCoreDataCurrent;
        this.demeterCoreDataLastMonth = demeterCoreDataLastMonth;
        this.demeterCoreDataLastYear = demeterCoreDataLastYear;
        this.tbRate = tbRate;
        this.hbRate = hbRate;
        this.foldLine = foldLine;
    }
}
