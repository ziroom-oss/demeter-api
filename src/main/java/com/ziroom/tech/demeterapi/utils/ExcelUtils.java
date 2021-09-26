package com.ziroom.tech.demeterapi.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ziroom.tech.demeterapi.dao.entity.DemeterCoreData;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

/**
 * @author libingsi
 * @date 2021/9/24 14:57
 */
@Slf4j
public class ExcelUtils {

    private final static String FORMAT_DATE = "yyyy-MM-dd";


    public static List<DemeterCoreData> readExcel(MultipartFile file) {
        List<DemeterCoreData> coreDataList = new ArrayList<>();
        XSSFWorkbook workbook = null;
        int sheetNum = 0;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
            sheetNum = workbook.getNumberOfSheets();

        } catch (Exception e) {
           log.error("解析核心数据excel失败" + e.getMessage());
        }

        Row row = null;
        for (int k = 0; k < sheetNum; k++) {

            Sheet sheet = workbook.getSheetAt(k);
            String sheetName = sheet.getSheetName();
            if ("核心指标数据".equals(sheetName)) {
                int j = 0;
                for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                    DemeterCoreData demeterCoreData = new DemeterCoreData();
                    row = sheet.getRow(i);
                    row.getCell(j).setCellType(CELL_TYPE_STRING);
                    String coreNameCell = checkType(row.getCell(j++));
                    String createTimeCell = checkType(row.getCell(j++));
                    String departmentCodeCell = checkType(row.getCell(j++));
                    String coreSysNameCell = checkType(row.getCell(j++));
                    String createUserCell = checkType(row.getCell(j++));
                    String coreType = checkType(row.getCell(j++));
                    Cell coreDataCell = row.getCell(j++);


                    j = 0;

                    if (StrUtil.isNotEmpty(coreNameCell)){
                        demeterCoreData.setCoreName(coreNameCell);
                    }

                    if (StrUtil.isNotEmpty(createTimeCell)){
                        demeterCoreData.setCreateTime(DateUtils.stringToDate(createTimeCell,FORMAT_DATE));
                    }
                    if (StrUtil.isNotEmpty(departmentCodeCell)){
                        demeterCoreData.setDepartmentCode(departmentCodeCell);
                    }
                    if (StrUtil.isNotEmpty(coreSysNameCell)){
                        demeterCoreData.setCoreSysName(coreSysNameCell);
                    }
                    if (StrUtil.isNotEmpty(createUserCell)){
                        demeterCoreData.setCreateUser(createUserCell);
                    }
                    if (StrUtil.isNotEmpty(coreType)){
                        demeterCoreData.setCoreType(coreType);
                    }
                    if (ObjectUtil.isNotEmpty(coreDataCell)) {
                        coreDataCell.setCellType(Cell.CELL_TYPE_STRING);
                        String stringCellValue = coreDataCell.getStringCellValue();
                        if (StrUtil.isNotEmpty(stringCellValue)){
                            BigDecimal bd = new BigDecimal(stringCellValue);
                            demeterCoreData.setCoreData(bd);
                        }
                    }
                    demeterCoreData.setUpdateTime(new Date());
                    coreDataList.add(demeterCoreData);

                }
            }
        }
        return coreDataList;

    }


    /**
     * 处理double类型的单元格
     *
     * @param cell
     * @return
     */
    public static String checkType(Cell cell) {
        String result = null;
        if (ObjectUtil.isNotEmpty(cell)) {
            CellType cellType = CellType.forInt(cell.getCellType());
            if (CellType.STRING.equals(cellType)) {
                result = cell.getStringCellValue().trim();
            } else if (CellType.NUMERIC.equals(cellType)) {
                Double numericCellValue = cell.getNumericCellValue();
                result = String.valueOf(numericCellValue.intValue()).trim();
            }
        }
        return result;
    }


}
