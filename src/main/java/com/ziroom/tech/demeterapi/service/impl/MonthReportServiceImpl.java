package com.ziroom.tech.demeterapi.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ziroom.tech.demeterapi.dao.entity.DemeterCoreData;
import com.ziroom.tech.demeterapi.dao.entity.DemeterCoreDataExample;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterCoreDataDao;
import com.ziroom.tech.demeterapi.po.dto.req.monthRept.DemeterCoreDataReq;
import com.ziroom.tech.demeterapi.po.dto.resp.monthRept.DemeterCoreDataResp;
import com.ziroom.tech.demeterapi.service.MonthReportService;
import com.ziroom.tech.demeterapi.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class MonthReportServiceImpl implements MonthReportService {

    @Resource
    private DemeterCoreDataDao demeterCoreDataDao;


    //处理筛选条件
    private DemeterCoreDataReq handleDataFilter(DemeterCoreDataReq demeterCoreDataReq){

        DemeterCoreDataReq demeterCoreData = new DemeterCoreDataReq();

        if (demeterCoreDataReq != null){
            String departmentCode = demeterCoreDataReq.getDepartmentCode();
            //如果部门code有值
            if (!StringUtil.isEmpty(departmentCode)){
                demeterCoreData.setDepartmentCode(departmentCode);
            } else {
                //设置默认值
                demeterCoreData.setDepartmentCode("10258");
            }

            //2021-09-01 00 ：00 ：00  获取前端传输日期  如果没传值则为当前月份
            DateTime createTimeto = StringUtil.isEmpty(demeterCoreDataReq.getCreateTimeEnd()) ? DateUtil.date(new Date()) : DateUtil.parseDate(demeterCoreDataReq.getCreateTimeEnd());
            //if(!StringUtil.isEmpty(createTimeto)){
                //找到本年第一个月
                String createTimeFrom = DateUtil.beginOfYear(createTimeto).toString("yyyy-MM-dd HH:mm:ss");
                demeterCoreData.setCreateTimeStart(createTimeFrom);
                //查询月份参数最后一个月|区间查找  between DateUtil.parse(demeterCoreDataReq.getCreateTimeEnd())
                demeterCoreData.setCreateTimeEnd(DateUtil.endOfMonth(createTimeto).toString("yyyy-MM-dd HH:mm:ss"));
//            } else {
//                demeterCoreData.setCreateTimeEnd(createTimeto.toString("yyyy-MM-dd HH:mm:ss"));
//                demeterCoreData.setCreateTimeStart(DateUtil.beginOfYear(new Date()).toString("yyyy-MM-dd HH:mm:ss"));
//            }
        }

        return demeterCoreData;
    }

    //获取筛选条件下所有系统数据 可存缓存
    private Map<String, List<DemeterCoreData>> getAllSysData(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq){

        DemeterCoreDataExample demeterCoreDataExample = new DemeterCoreDataExample();
        demeterCoreDataExample.createCriteria()
                .andCoreTypeEqualTo("1")
                .andDepartmentCodeEqualTo(demeterCoreDataReq.getDepartmentCode())
              //  .andCoreNameEqualTo(coreNameReq)
                .andCreateTimeBetween(DateUtil.parse(demeterCoreDataReq.getCreateTimeStart()), DateUtil.parse(demeterCoreDataReq.getCreateTimeEnd()));
        List<DemeterCoreData> demeterCoreDatas = demeterCoreDataDao.selectByExample(demeterCoreDataExample);

        Map<String, List<DemeterCoreData>> demeterCoreDatasMap = demeterCoreDatas.stream().collect(groupingBy(DemeterCoreData::getCoreName));
        return demeterCoreDatasMap;
    }

    //
    private Map<String, List<DemeterCoreData>> busSupportData(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq){
        DemeterCoreDataReq coreDataReq = this.handleDataFilter(demeterCoreDataReq);
        DemeterCoreDataExample demeterCoreDataExample = new DemeterCoreDataExample();
        demeterCoreDataExample.createCriteria()
                .andCoreTypeEqualTo("2")
                .andDepartmentCodeEqualTo(coreDataReq.getDepartmentCode())
                //.andCoreNameEqualTo(coreNameReq)
                .andCreateTimeBetween(DateUtil.parse(coreDataReq.getCreateTimeStart()), DateUtil.parse(coreDataReq.getCreateTimeEnd()));
        List<DemeterCoreData> demeterCoreDatas = demeterCoreDataDao.selectByExample(demeterCoreDataExample);

        Map<String, List<DemeterCoreData>> demeterCoreDatasMap = demeterCoreDatas.stream().collect(groupingBy(DemeterCoreData::getCoreName));
        return demeterCoreDatasMap;
    }
    //
    private Map<String, List<DemeterCoreData>> devEffiTollData(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq){
        DemeterCoreDataReq coreDataReq = this.handleDataFilter(demeterCoreDataReq);
        DemeterCoreDataExample demeterCoreDataExample = new DemeterCoreDataExample();
        demeterCoreDataExample.createCriteria()
                .andCoreTypeEqualTo("3")
                .andDepartmentCodeEqualTo(coreDataReq.getDepartmentCode())
                //.andCoreNameEqualTo(coreNameReq)
                .andCreateTimeBetween(DateUtil.parse(coreDataReq.getCreateTimeStart()), DateUtil.parse(coreDataReq.getCreateTimeEnd()));
        List<DemeterCoreData> demeterCoreDatas = demeterCoreDataDao.selectByExample(demeterCoreDataExample);

        Map<String, List<DemeterCoreData>> demeterCoreDatasMap = demeterCoreDatas.stream().collect(groupingBy(DemeterCoreData::getCoreName));
        return demeterCoreDatasMap;
    }
    //
    private Map<String, List<DemeterCoreData>> oapSupportData(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq){
        DemeterCoreDataReq coreDataReq = this.handleDataFilter(demeterCoreDataReq);
        DemeterCoreDataExample demeterCoreDataExample = new DemeterCoreDataExample();
        demeterCoreDataExample.createCriteria()
                .andCoreTypeEqualTo("4")
                .andDepartmentCodeEqualTo(coreDataReq.getDepartmentCode())
                //.andCoreNameEqualTo(coreNameReq)
                .andCreateTimeBetween(DateUtil.parse(coreDataReq.getCreateTimeStart()), DateUtil.parse(coreDataReq.getCreateTimeEnd()));
        List<DemeterCoreData> demeterCoreDatas = demeterCoreDataDao.selectByExample(demeterCoreDataExample);

        Map<String, List<DemeterCoreData>> demeterCoreDatasMap = demeterCoreDatas.stream().collect(groupingBy(DemeterCoreData::getCoreName));
        return demeterCoreDatasMap;
    }

    //环比筛选
    private DemeterCoreData getHbData(String departCode, String coreNameReq, DateTime hbDateTime, String coreType){

        DateTime hbDateBegain = DateUtil.beginOfMonth(hbDateTime);
        DateTime hbDateEnd = DateUtil.endOfMonth(hbDateTime);

        DemeterCoreDataExample demeterCoreDataExample = new DemeterCoreDataExample();
        demeterCoreDataExample.createCriteria()
                .andCoreTypeNotEqualTo(coreType)
                .andDepartmentCodeEqualTo(departCode)
                //.andCoreNameEqualTo(coreNameReq)
                .andCreateTimeBetween(hbDateBegain, hbDateEnd);
        List<DemeterCoreData> demeterCoreDatas = demeterCoreDataDao.selectByExample(demeterCoreDataExample);

        return demeterCoreDatas.size() <= 0 ? null : demeterCoreDatas.get(0);
    }

    //处理逻辑相似
    private Map<String, DemeterCoreDataResp> assembData(DemeterCoreDataReq demeterCoreDataReq, Map<String, List<DemeterCoreData>> demeterCoreMap, DemeterCoreData hbCoreData){

        //同比时间
        DateTime tbDateTime = DateUtil.offsetMonth(DateUtil.parse(demeterCoreDataReq.getCreateTimeEnd()), -1);

        //SLA  、、、 2021 01
        //SLA  、、、 2021 02
        //SLA  、、、 2021 03
        //SLA  、、、 2021 04
        //SLA  、、、 2021 05
        Map<String, DemeterCoreDataResp> demeterCoreMaps = new HashMap<>();
        //coreNameStr为名称
        demeterCoreMap.forEach((coreNameStr, demeterCoreDatas) -> {

            DemeterCoreDataResp coreDataResp = new DemeterCoreDataResp();
            String tbDateTimeStr = DateUtil.format(tbDateTime, "yyyy-MM");
           // String hbDateTimeStr = DateUtil.format(hbCoreData.getCreateTime(), "yyyy-MM");
            String curCreateStr = DateUtil.format(DateUtil.parse(demeterCoreDataReq.getCreateTimeEnd()), "yyyy-MM");
            List<String> foldLine = new ArrayList<>();
            demeterCoreDatas.stream().forEach(demeterCoreData -> {

                    String createTimeStr = DateUtil.format(demeterCoreData.getCreateTime(), "yyyy-MM");
                    BigDecimal coreData = demeterCoreData.getCoreData();

                    if (createTimeStr.equals(tbDateTimeStr)){// tbDateTime 同比时间
                        coreDataResp.setDemeterCoreDataLastMonth(demeterCoreData);
                        coreDataResp.setTb(coreData);

                    }

                    if (createTimeStr.equals(curCreateStr)){//当前时间
                        coreDataResp.setDemeterCoreDataCurrent(demeterCoreData);
                        coreDataResp.setCur(coreData);
                    }

                foldLine.add(demeterCoreData.getCoreData() != null ? demeterCoreData.getCoreData().toString() : "0");
                });

            //前提是当月有数据
            if (!StringUtil.isEmpty(coreDataResp.getCur())) {
                if (coreDataResp.getTb() != null && coreDataResp.getTb().compareTo(BigDecimal.ZERO)!=0){
                    //设置同比数据
                    BigDecimal tbRate = (coreDataResp.getCur().subtract(coreDataResp.getTb())).divide(coreDataResp.getTb(), 5, BigDecimal.ROUND_UP);
                    coreDataResp.setTbRate(String.valueOf(tbRate));
                } else {
                    coreDataResp.setTbRate("0");
                    coreDataResp.setDemeterCoreDataLastMonth(new DemeterCoreData());
                }
                //环比数据单独查询因为环比是上一年无法在本年截止到当月时间找到
                if (hbCoreData != null) {
                    coreDataResp.setHb(hbCoreData.getCoreData());
                    if(coreDataResp.getHb() != null && coreDataResp.getHb().compareTo(BigDecimal.ZERO)!=0){
                        //设置环比数据
                        BigDecimal hbRate = (coreDataResp.getCur().subtract(coreDataResp.getHb())).divide(coreDataResp.getHb(), 5, BigDecimal.ROUND_UP);
                        coreDataResp.setHbRate(String.valueOf(hbRate));
                    } else {
                        coreDataResp.setHbRate("0");
                    }
                    coreDataResp.setDemeterCoreDataLastYear(hbCoreData);
                } else {
                    coreDataResp.setHbRate("0");
                    coreDataResp.setDemeterCoreDataLastYear(new DemeterCoreData());
                }

                //设置曲线集合
                coreDataResp.setFoldLine(foldLine);

                demeterCoreMaps.put(coreNameStr, coreDataResp);
            } else {
                demeterCoreMaps.put(coreNameStr, coreDataResp);
            }

        });
        return demeterCoreMaps;
    }


    @Override
    public Map<String, DemeterCoreDataResp> getSLA(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq) {

        DemeterCoreDataReq coreDataReq = this.handleDataFilter(demeterCoreDataReq);
        //环比时间
        DateTime hbDateTime = DateUtil.offsetMonth(DateUtil.parse(coreDataReq.getCreateTimeEnd()), -12);

        //所有筛选条件下的内部数据
        //1月|2月|3月|4月|5月 同比
        Map<String, List<DemeterCoreData>> allSysData = this.getAllSysData(coreDataReq, coreNameReq);
        //环比时间
        DemeterCoreData hbDemeterCoreData = this.getHbData(coreDataReq.getDepartmentCode(), coreNameReq, hbDateTime, "1");
        Map<String, DemeterCoreDataResp> demeterCoreDataRespMap = assembData(coreDataReq, allSysData, hbDemeterCoreData);
        return demeterCoreDataRespMap;
    }

    @Override
    public Map<String, DemeterCoreDataResp> getBusSupport(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq) {

        DemeterCoreDataReq coreDataReq = this.handleDataFilter(demeterCoreDataReq);
        //环比时间
        DateTime hbDateTime = DateUtil.offsetMonth(DateUtil.parse(coreDataReq.getCreateTimeEnd()), -12);

        //所有筛选条件下的内部数据
        //1月|2月|3月|4月|5月 同比
        Map<String, List<DemeterCoreData>> allSysData = this.busSupportData(coreDataReq, coreNameReq);
        //环比时间
        DemeterCoreData hbDemeterCoreData = this.getHbData(coreDataReq.getDepartmentCode(), coreNameReq, hbDateTime, "1");
        Map<String, DemeterCoreDataResp> demeterCoreDataRespMap = assembData(coreDataReq, allSysData, hbDemeterCoreData);
        return demeterCoreDataRespMap;
    }

    @Override
    public Map<String, DemeterCoreDataResp> getDevEffiToll(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq) {
        DemeterCoreDataReq coreDataReq = this.handleDataFilter(demeterCoreDataReq);
        //环比时间
        DateTime hbDateTime = DateUtil.offsetMonth(DateUtil.parse(coreDataReq.getCreateTimeEnd()), -12);

        //所有筛选条件下的内部数据
        //1月|2月|3月|4月|5月 同比
        Map<String, List<DemeterCoreData>> allSysData = this.devEffiTollData(coreDataReq, coreNameReq);
        //环比时间
        DemeterCoreData hbDemeterCoreData = this.getHbData(coreDataReq.getDepartmentCode(), coreNameReq, hbDateTime, "1");
        Map<String, DemeterCoreDataResp> demeterCoreDataRespMap = assembData(coreDataReq, allSysData, hbDemeterCoreData);
        return demeterCoreDataRespMap;
    }

    @Override
    public Map<String, DemeterCoreDataResp> getOapSupport(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq) {
        DemeterCoreDataReq coreDataReq = this.handleDataFilter(demeterCoreDataReq);
        //环比时间
        DateTime hbDateTime = DateUtil.offsetMonth(DateUtil.parse(coreDataReq.getCreateTimeEnd()), -12);

        //所有筛选条件下的内部数据
        //1月|2月|3月|4月|5月 同比
        Map<String, List<DemeterCoreData>> allSysData = this.oapSupportData(coreDataReq, coreNameReq);
        //环比时间
        DemeterCoreData hbDemeterCoreData = this.getHbData(coreDataReq.getDepartmentCode(), coreNameReq, hbDateTime, "1");
        Map<String, DemeterCoreDataResp> demeterCoreDataRespMap = assembData(coreDataReq, allSysData, hbDemeterCoreData);
        return demeterCoreDataRespMap;
    }

}
