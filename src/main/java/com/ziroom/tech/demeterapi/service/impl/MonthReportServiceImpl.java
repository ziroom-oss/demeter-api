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
                demeterCoreData.setDepartmentCode("102558");
            }

            //2021-09-01 00 ：00 ：00  获取前端传输日期
            DateTime createTimeto = DateUtil.parseDate(demeterCoreDataReq.getCreateTimeEnd());
            if(!StringUtil.isEmpty(createTimeto)){
                //找到本年第一个月
                String createTimeFrom = DateUtil.beginOfYear(createTimeto).toString("yyyy-MM-dd HH:mm:ss");
                demeterCoreData.setCreateTimeStart(createTimeFrom);
                //查询月份参数最后一个月|区间查找  between
                demeterCoreData.setCreateTimeEnd(DateUtil.endOfMonth(DateUtil.parse(demeterCoreDataReq.getCreateTimeEnd())).toString("yyyy-MM-dd HH:mm:ss"));
            } else {
                demeterCoreData.setCreateTimeEnd(DateUtil.endOfMonth(new Date()).toString("yyyy-MM-dd HH:mm:ss"));
                demeterCoreData.setCreateTimeStart(DateUtil.beginOfYear(new Date()).toString("yyyy-MM-dd HH:mm:ss"));
            }
        }

        return demeterCoreData;
    }

    //获取筛选条件下所有系统数据 可存缓存
    private List<DemeterCoreData> getAllSysData(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq){

        DemeterCoreDataReq coreDataReq = this.handleDataFilter(demeterCoreDataReq);
        DemeterCoreDataExample demeterCoreDataExample = new DemeterCoreDataExample();
        demeterCoreDataExample.createCriteria()
                .andCoreTypeEqualTo("1")
                .andDepartmentCodeEqualTo(coreDataReq.getDepartmentCode())
                .andCoreNameEqualTo(coreNameReq)
                .andCreateTimeBetween(DateUtil.parse(coreDataReq.getCreateTimeStart()), DateUtil.parse(coreDataReq.getCreateTimeEnd()));
        List<DemeterCoreData> demeterCoreDatas = demeterCoreDataDao.selectByExample(demeterCoreDataExample);
        return demeterCoreDatas;
    }

    //获取筛选条件下所有非系统数据 可存缓存
    private List<DemeterCoreData> getAllUnSysData(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq){
        DemeterCoreDataReq coreDataReq = this.handleDataFilter(demeterCoreDataReq);
        DemeterCoreDataExample demeterCoreDataExample = new DemeterCoreDataExample();
        demeterCoreDataExample.createCriteria()
                .andCoreTypeNotEqualTo("1")
                .andDepartmentCodeEqualTo(coreDataReq.getDepartmentCode())
                .andCoreNameEqualTo(coreNameReq)
                .andCreateTimeBetween(DateUtil.parse(coreDataReq.getCreateTimeStart()), DateUtil.parse(coreDataReq.getCreateTimeEnd()));
        List<DemeterCoreData> demeterCoreDatas = demeterCoreDataDao.selectByExample(demeterCoreDataExample);
        return demeterCoreDatas;
    }

    //环比筛选
    private DemeterCoreData getHbData(String departCode, String coreNameReq, DateTime hbDateTime, String coreType){

        DateTime hbDateBegain = DateUtil.beginOfMonth(hbDateTime);
        DateTime hbDateEnd = DateUtil.endOfMonth(hbDateTime);

        DemeterCoreDataExample demeterCoreDataExample = new DemeterCoreDataExample();
        demeterCoreDataExample.createCriteria()
                .andCoreTypeEqualTo(coreType)
                .andDepartmentCodeEqualTo(departCode)
                .andCoreNameEqualTo(coreNameReq)
                .andCreateTimeBetween(hbDateBegain, hbDateEnd);
        List<DemeterCoreData> demeterCoreDatas = demeterCoreDataDao.selectByExample(demeterCoreDataExample);

        return demeterCoreDatas.size() <= 0 ? null : demeterCoreDatas.get(0);
    }

    //处理逻辑相似
    private DemeterCoreDataResp assembData(DemeterCoreDataReq demeterCoreDataReq, List<DemeterCoreData> coreDataList, DemeterCoreData hbCoreData){

        //同比时间
        DateTime tbDateTime = DateUtil.offsetMonth(DateUtil.parse(demeterCoreDataReq.getCreateTimeEnd()), -1);

        //SLA  、、、 2021 01
        //SLA  、、、 2021 02
        //SLA  、、、 2021 03
        //SLA  、、、 2021 04
        //SLA  、、、 2021 05
        //Map<String, List<DemeterCoreData>> demeterCoreMap = coreDataList.stream().collect(groupingBy(DemeterCoreData::getCoreName));
        //coreNameStr为名称
      //  demeterCoreMap.forEach((coreNameStr, demeterCoreDatas) -> {
        DemeterCoreDataResp coreDataResp = new DemeterCoreDataResp();

        String tbDateTimeStr = DateUtil.format(tbDateTime, "yyyy-MM");
       // String hbDateTimeStr = DateUtil.format(hbCoreData.getCreateTime(), "yyyy-MM");
        String curCreateStr = DateUtil.format(DateUtil.parse(demeterCoreDataReq.getCreateTimeEnd()), "yyyy-MM");
        List<String> foldLine = new ArrayList<>();
        coreDataList.stream().forEach(demeterCoreData -> {

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

                foldLine.add(demeterCoreData.getCoreData().toString());
            });

          if (coreDataResp.getTb() != null && coreDataResp.getTb().compareTo(BigDecimal.ZERO)!=0){
              //设置同比数据
              BigDecimal tbRate = (coreDataResp.getCur().subtract(coreDataResp.getTb())).divide(coreDataResp.getTb(), 5, BigDecimal.ROUND_UP);
              coreDataResp.setTbRate(String.valueOf(tbRate));
          } else {
              coreDataResp.setTbRate("0");
          }

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


   //     });
        return coreDataResp;
    }


    @Override
    public DemeterCoreDataResp getSLA(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq) {
        //环比时间
        DateTime hbDateTime = DateUtil.offsetMonth(DateUtil.parse(demeterCoreDataReq.getCreateTimeEnd()), -12);

        //所有筛选条件下的内部数据
        //1月|2月|3月|4月|5月 同比
        List<DemeterCoreData> allSysData = this.getAllSysData(demeterCoreDataReq, coreNameReq);
        //环比时间
        DemeterCoreData hbDemeterCoreData = this.getHbData(demeterCoreDataReq.getDepartmentCode(), coreNameReq, hbDateTime, "1");
        DemeterCoreDataResp demeterCoreDataResp = assembData(demeterCoreDataReq, allSysData, hbDemeterCoreData);
        return demeterCoreDataResp;
    }

    @Override
    public DemeterCoreDataResp getBusSupport(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq) {
        //环比时间
        DateTime hbDateTime = DateUtil.offsetMonth(DateUtil.parse(demeterCoreDataReq.getCreateTimeEnd()), -12);

        //业务支持
        List<DemeterCoreData> coreDataList = this.getAllUnSysData(demeterCoreDataReq, coreNameReq).stream().filter(demeterCoreData -> {
            return demeterCoreData.getCoreType().equals("2");
        }).collect(Collectors.toList());
        DemeterCoreData hbDemeterCoreData = this.getHbData(demeterCoreDataReq.getDepartmentCode(), coreNameReq, hbDateTime, "2");
        DemeterCoreDataResp demeterCoreDataResp = assembData(demeterCoreDataReq, coreDataList, hbDemeterCoreData);
        demeterCoreDataResp.setDemeterCoreDataLastYear(hbDemeterCoreData);
        return demeterCoreDataResp;
    }

    @Override
    public DemeterCoreDataResp getDevEffiToll(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq) {
        //环比时间
        DateTime hbDateTime = DateUtil.offsetMonth(DateUtil.parse(demeterCoreDataReq.getCreateTimeEnd()), -12);

        //开发效能工具
        List<DemeterCoreData> coreDataList = this.getAllUnSysData(demeterCoreDataReq, coreNameReq).stream().filter(demeterCoreData -> {
            return demeterCoreData.getCoreType().equals("3");
        }).collect(Collectors.toList());
        DemeterCoreData hbDemeterCoreData = this.getHbData(demeterCoreDataReq.getDepartmentCode(), coreNameReq, hbDateTime, "3");
       DemeterCoreDataResp demeterCoreDataResp = assembData(demeterCoreDataReq, coreDataList, hbDemeterCoreData);
        demeterCoreDataResp.setDemeterCoreDataLastYear(hbDemeterCoreData);
        return demeterCoreDataResp;
    }

    @Override
    public DemeterCoreDataResp getOapSupport(DemeterCoreDataReq demeterCoreDataReq, String coreNameReq) {
        //环比时间
        DateTime hbDateTime = DateUtil.offsetMonth(DateUtil.parse(demeterCoreDataReq.getCreateTimeEnd()), -12);

        //运维支持数据
        List<DemeterCoreData> coreDataList = this.getAllUnSysData(demeterCoreDataReq, coreNameReq).stream().filter(demeterCoreData -> {
            return demeterCoreData.getCoreType().equals("4");
        }).collect(Collectors.toList());
        DemeterCoreData hbDemeterCoreData = this.getHbData(demeterCoreDataReq.getDepartmentCode(), coreNameReq, hbDateTime, "4");
        DemeterCoreDataResp demeterCoreDataResp = assembData(demeterCoreDataReq, coreDataList, hbDemeterCoreData);
        demeterCoreDataResp.setDemeterCoreDataLastYear(hbDemeterCoreData);
        return demeterCoreDataResp;
    }

}
