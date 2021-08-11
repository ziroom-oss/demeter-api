package com.ziroom.tech.demeterapi.service.impl;

import com.ziroom.tech.demeterapi.common.BeanMapper;
import com.ziroom.tech.demeterapi.dao.entity.DemeterUserEmail;
import com.ziroom.tech.demeterapi.dao.entity.DemeterUserEmailExample;
import com.ziroom.tech.demeterapi.dao.mapper.DemeterUserEmailDao;
import com.ziroom.tech.demeterapi.po.dto.req.email.UserEmailDto;
import com.ziroom.tech.demeterapi.po.qo.UserEmailQo;
import com.ziroom.tech.demeterapi.service.UserEmailService;
import com.ziroom.tech.demeterapi.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author libingsi
 * @date 2021/8/4 17:34
 */
@Slf4j
@Service
public class UserEmailServiceImpl implements UserEmailService {

    @Autowired
    private DemeterUserEmailDao demeterUserEmailDao;

    @Autowired
    private BeanMapper beanMapper;

    @Override
    @Transactional
    public Integer insertEmail(UserEmailQo qo) {
        DemeterUserEmail emailEntity = new DemeterUserEmail();
        emailEntity.setEmail(qo.getEmail());
        emailEntity.setSubEmail(qo.getSubEmail());
        emailEntity.setCreateTime(new Date());
        emailEntity.setUpdateTime(new Date());
        return demeterUserEmailDao.insert(emailEntity);
    }

    @Override
    public Integer deleteEmail(Long id) {
        return demeterUserEmailDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<UserEmailDto> selectSubEmail(UserEmailQo qo) {
        DemeterUserEmailExample demeterRoleExample = new DemeterUserEmailExample();
        demeterRoleExample.createCriteria().andEmailEqualTo(qo.getEmail());
        List<DemeterUserEmail> demeterUserEmails = demeterUserEmailDao.selectByExample(demeterRoleExample);
        List<UserEmailDto> dtos =beanMapper.mapList(demeterUserEmails,UserEmailDto.class);
        return dtos;
    }

    @Override
    public List<UserEmailDto> batchSelectEmail(List<String> emailList) {
        DemeterUserEmailExample demeterUserEmailExample = new DemeterUserEmailExample();
        demeterUserEmailExample.createCriteria()
                .andEmailIn(emailList);
        List<DemeterUserEmail> demeterUserEmails = demeterUserEmailDao.selectByExample(demeterUserEmailExample);
        return beanMapper.mapList(demeterUserEmails,UserEmailDto.class);
    }
}
