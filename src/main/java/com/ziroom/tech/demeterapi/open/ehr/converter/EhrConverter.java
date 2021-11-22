package com.ziroom.tech.demeterapi.open.ehr.converter;

import com.ziroom.tech.demeterapi.open.ehr.dto.EhrDepartmentInfoDto;
import com.ziroom.tech.demeterapi.open.ehr.dto.EhrUserInfoDto;
import com.ziroom.tech.demeterapi.open.ehr.vo.EhrDepartmentInfoVO;
import com.ziroom.tech.demeterapi.open.ehr.vo.EhrUserInfoVO;

import java.util.function.Function;

/**
 * ehr model 转换器
 * @author xuzeyu
 */
public class EhrConverter {

    /**
     * 个人基本信息model转换
     * @return
     */
    public static Function<EhrUserInfoDto, EhrUserInfoVO> EhrUserInfoConverter() {
        return portraitPersonUserInfoDto -> {
            EhrUserInfoVO ehrUserInfoVO = new EhrUserInfoVO();
            ehrUserInfoVO.setUsername(portraitPersonUserInfoDto.getUsername());
            ehrUserInfoVO.setEmail(portraitPersonUserInfoDto.getEmail());
            ehrUserInfoVO.setEducation(portraitPersonUserInfoDto.getEducation());
            ehrUserInfoVO.setHireDays(portraitPersonUserInfoDto.getHireDays());
            ehrUserInfoVO.setJob(portraitPersonUserInfoDto.getJob());
            ehrUserInfoVO.setPosition(portraitPersonUserInfoDto.getPosition());
            ehrUserInfoVO.setSkills(portraitPersonUserInfoDto.getSkills());
            return ehrUserInfoVO;
        };
    }

    /**
     * 部门信息model转换
     * @return
     */
    public static Function<EhrDepartmentInfoDto, EhrDepartmentInfoVO> EhrDepartmentInfoConverter() {
        return ehrDepartmentInfoDto -> {
            EhrDepartmentInfoVO ehrDepartmentInfoVO = new EhrDepartmentInfoVO();
            ehrDepartmentInfoVO.setDepartmentCode(ehrDepartmentInfoDto.getDepartmentCode());
            ehrDepartmentInfoVO.setDepartmentName(ehrDepartmentInfoDto.getDepartmentName());
            return ehrDepartmentInfoVO;
        };
    }
}
