package com.ziroom.tech.demeterapi.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ziroom.tech.demeterapi.dao.entity.DemeterCoreData;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterCoreDataDao;
import com.ziroom.tech.demeterapi.service.InitCoreDataService;
import com.ziroom.tech.demeterapi.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * @author libingsi
 * @date 2021/9/24 下午6:52
 */
@Service
@Slf4j
public class InitCoreDataServiceImpl implements InitCoreDataService {


    @Autowired
    private DemeterCoreDataDao dataDao;

    @Override
    public void initData(MultipartFile file) {
        List<DemeterCoreData> coreData = ExcelUtils.readExcel(file);
        if (CollectionUtil.isEmpty(coreData)){
            return;
        }
        coreData.forEach(v -> dataDao.insert(v));
    }
}
