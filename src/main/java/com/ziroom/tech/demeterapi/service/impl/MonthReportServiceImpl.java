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
import java.util.concurrent.atomic.AtomicInteger;
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
    private List<DemeterCoreData> getAllSysData(DemeterCoreDataReq demeterCoreDataReq){

        DemeterCoreDataReq coreDataReq = this.handleDataFilter(demeterCoreDataReq);
        DemeterCoreDataExample demeterCoreDataExample = new DemeterCoreDataExample();
        demeterCoreDataExample.createCriteria()
                .andCoreTypeEqualTo("1")
                .andDepartmentCodeEqualTo(coreDataReq.getDepartmentCode())
                .andCreateTimeBetween(DateUtil.parse(coreDataReq.getCreateTimeStart()), DateUtil.parse(coreDataReq.getCreateTimeEnd()));
        List<DemeterCoreData> demeterCoreDatas = demeterCoreDataDao.selectByExample(demeterCoreDataExample);
        return demeterCoreDatas;
    }

    //获取筛选条件下所有非系统数据 可存缓存
    private List<DemeterCoreData> getAllUnSysData(DemeterCoreDataReq demeterCoreDataReq){
        DemeterCoreDataReq coreDataReq = this.handleDataFilter(demeterCoreDataReq);
        DemeterCoreDataExample demeterCoreDataExample = new DemeterCoreDataExample();
        demeterCoreDataExample.createCriteria()
                .andCoreTypeNotEqualTo("1")
                .andDepartmentCodeEqualTo(coreDataReq.getDepartmentCode())
                .andCreateTimeBetween(DateUtil.parse(coreDataReq.getCreateTimeStart()), DateUtil.parse(coreDataReq.getCreateTimeEnd()));
        List<DemeterCoreData> demeterCoreDatas = demeterCoreDataDao.selectByExample(demeterCoreDataExample);
        return demeterCoreDatas;
    }

    //处理逻辑相似
    private Map<String, DemeterCoreDataResp> assembData(DemeterCoreDataReq demeterCoreDataReq, List<DemeterCoreData> coreDataList){

        Map<String, DemeterCoreDataResp> assembDataMap = new HashMap<>();
        //同比时间
        DateTime tbDateTime = DateUtil.offsetMonth(DateUtil.parse(demeterCoreDataReq.getCreateTimeEnd()), -1);
        //环比时间
        DateTime hbDateTime = DateUtil.offsetMonth(DateUtil.parse(demeterCoreDataReq.getCreateTimeEnd()), -12);
        //SLA  、、、 2021 01   慢sql数量  、、、 2021 01
        //SLA  、、、 2021 02   慢sql数量  、、、 2021 02
        //SLA  、、、 2021 03   慢sql数量  、、、 2021 03
        //SLA  、、、 2021 04
        //SLA  、、、 2021 05   慢sql数量  、、、 2021 03
        Map<String, List<DemeterCoreData>> demeterCoreMap = coreDataList.stream().collect(groupingBy(DemeterCoreData::getCoreName));
        //coreNameStr为名称
        demeterCoreMap.forEach((coreNameStr, demeterCoreDatas) -> {
            DemeterCoreDataResp coreDataResp = new DemeterCoreDataResp();
            List<String> foldLine = new ArrayList<>();

            BigDecimal tbData = new BigDecimal(0);
            BigDecimal hbData = new BigDecimal(0);
            BigDecimal curData = new BigDecimal(0);

            demeterCoreDatas.stream().forEach(demeterCoreData -> {

                String createTimeStr = DateUtil.format(demeterCoreData.getCreateTime(), "yyyy-MM");
                String tbDateTimeStr = DateUtil.format(tbDateTime, "yyyy-MM");
                String hbDateTimeStr = DateUtil.format(hbDateTime, "yyyy-MM");
                String curCreateStr = DateUtil.format(DateUtil.parse(demeterCoreDataReq.getCreateTimeEnd()), "yyyy-MM");

                if (createTimeStr.equals(tbDateTimeStr)){// tbDateTime 同比时间
                    coreDataResp.setDemeterCoreDataLastMonth(demeterCoreData);
                    tbData.add(demeterCoreData.getCoreData());
                }
                if (createTimeStr.equals(hbDateTimeStr)){// hbDateTime 环比时间
                    coreDataResp.setDemeterCoreDataLastYear(demeterCoreData);
                    hbData.add(demeterCoreData.getCoreData());
                }
                if (createTimeStr.equals(curCreateStr)){//当前时间
                    coreDataResp.setDemeterCoreDataCurrent(demeterCoreData);
                    curData.add(demeterCoreData.getCoreData());
                }

                foldLine.add(demeterCoreData.getCoreData().toString());
            });

          if (tbData.intValue() != 0){
              //设置同比数据
              BigDecimal tbRate = (curData.subtract(tbData)).divide(tbData, 5, BigDecimal.ROUND_UP);
              coreDataResp.setTbRate(String.valueOf(tbRate.intValue()));
          } else {
              coreDataResp.setTbRate("0");
          }
            if(hbData.intValue() != 0){
                //设置环比数据
                BigDecimal hbRate = (curData.subtract(hbData)).divide(hbData, 5, BigDecimal.ROUND_UP);
                coreDataResp.setTbRate(String.valueOf(hbRate.intValue()));
            } else {
                coreDataResp.setHbRate("0");
            }
            //设置曲线集合
            coreDataResp.setFoldLine(foldLine);

            assembDataMap.put(coreNameStr,coreDataResp);
        });
        return assembDataMap;
    }


    @Override
    public Map<String, DemeterCoreDataResp> getSLA(DemeterCoreDataReq demeterCoreDataReq) {
        //所有筛选条件下的内部数据
        //1月|2月|3月|4月|5月 同比环比通用
        List<DemeterCoreData> allSysData = this.getAllSysData(demeterCoreDataReq);

        Map<String, DemeterCoreDataResp> demeterCoreDataRespMap = assembData(demeterCoreDataReq, allSysData);
        return demeterCoreDataRespMap;
    }

    @Override
    public Map<String, DemeterCoreDataResp> getBusSupport(DemeterCoreDataReq demeterCoreDataReq) {
        //业务支持
        List<DemeterCoreData> coreDataList = this.getAllUnSysData(demeterCoreDataReq).stream().filter(demeterCoreData -> {
            return demeterCoreData.getCoreType().equals("2");
        }).collect(Collectors.toList());
        Map<String, DemeterCoreDataResp> demeterCoreDataRespMap = assembData(demeterCoreDataReq, coreDataList);
        return demeterCoreDataRespMap;
    }

    @Override
    public Map<String, DemeterCoreDataResp> getDevEffiToll(DemeterCoreDataReq demeterCoreDataReq) {
        //开发效能工具
        List<DemeterCoreData> coreDataList = this.getAllUnSysData(demeterCoreDataReq).stream().filter(demeterCoreData -> {
            return demeterCoreData.getCoreType().equals("3");
        }).collect(Collectors.toList());
        Map<String, DemeterCoreDataResp> demeterCoreDataRespMap = assembData(demeterCoreDataReq, coreDataList);
        return demeterCoreDataRespMap;
    }

    @Override
    public Map<String, DemeterCoreDataResp> getOapSupport(DemeterCoreDataReq demeterCoreDataReq) {
        //运维支持数据
        List<DemeterCoreData> coreDataList = this.getAllUnSysData(demeterCoreDataReq).stream().filter(demeterCoreData -> {
            return demeterCoreData.getCoreType().equals("4");
        }).collect(Collectors.toList());
        Map<String, DemeterCoreDataResp> demeterCoreDataRespMap = assembData(demeterCoreDataReq, coreDataList);
        return demeterCoreDataRespMap;
    }

}
