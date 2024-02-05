package com.example.mediumcode23.controller;

import com.example.mediumcode23.listener.ReadEasyExeclAsyncListener;
import com.example.mediumcode23.pojo.UserInfo;
import com.example.mediumcode23.service.ReadEasyExeclService;
import com.example.mediumcode23.service.ReadExcelCacheAsyncService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@RestController
public class ReadEasyExcelController {
    private static final Logger logger = LogManager.getLogger(ReadEasyExcelController.class);

    @Autowired
    private ReadEasyExeclService readEasyExeclService;

    @Autowired
    private ReadExcelCacheAsyncService readExcelCacheAsyncService;

    private Set<UserInfo> userInfoCache;


    private static final int batchCount = 100;

    @RequestMapping(value = "/readEasyExcel", method = RequestMethod.GET)
    @ResponseBody
    public String readEasyExcel() {
        try {
            String path = "d:\\test\\data\\";
            File directory = new File(path);
            String[] xlsxArr = directory.list((dir, name) -> name.endsWith(".xlsx"));
            if (xlsxArr != null) {
                for (String fileName : xlsxArr) {
                    String filePath = path + fileName;
                    readExcelAndSaveAsync(filePath, fileName);
                }
            }
        } catch (Exception e) {
            logger.error("readEasyExcel 异常：", e);
            return "error";
        }
        return "success";
    }

    @RequestMapping(value = "/readExcelCacheAsync", method = RequestMethod.POST)
    @ResponseBody
    public String readExcelCacheAsync() {
        String path = "D:\\测试\\data\\";
        try {
            // 在读取Excel之前，缓存所有数据
//            USER_INFO_SET = getUserInfo();

            File file = new File(path);
            String[] xlsxArr = file.list();
            for (int i = 0; i < xlsxArr.length; i++) {
                File fileTemp = new File(path + "\\" + xlsxArr[i]);
                String filename = fileTemp.getName().replace(".xlsx", "");
                readExcelCacheAsyncService.readXls(path + filename + ".xlsx", filename);
            }
        } catch (Exception e) {
            logger.error("|#ReadDBCsv|#异常: ", e);
            return "error";
        }
        return "success";
    }


    @Async("async-executor")
    public void readExcelAndSaveAsync(String filePath, String tableName) {
        try (FileInputStream fis = new FileInputStream(filePath);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            int maxRow = sheet.getLastRowNum();
            logger.info(tableName + ".xlsx, a total of " + (maxRow + 1) + " rows of data!");

            List<UserInfo> list = new ArrayList<>();
            for (int rowNum = 1; rowNum <= maxRow; rowNum++) { // Skip header row
                XSSFRow row = sheet.getRow(rowNum);
                if (row == null) continue; // Skip empty rows

                UserInfo userInfo = createUserFromRow(row);
                list.add(userInfo);

                if (list.size() >= batchCount) {
                    readEasyExeclService.saveDataBatch(list);
                    list.clear();
                }
            }

            if (!list.isEmpty()) {
                readEasyExeclService.saveDataBatch(list);
            }
        } catch (FileNotFoundException e) {
            logger.error("File not found: " + filePath, e);
        } catch (IOException e) {
            logger.error("Error reading Excel file: " + filePath, e);
        } catch (Exception e) {
            logger.error("Unexpected error: ", e);
        }
    }

    private UserInfo createUserFromRow(XSSFRow row) {
        UserInfo userInfo = new UserInfo();
        // Assuming the order: ID, Name, Age, Address, Phone
        userInfo.setUuid(UUID.randomUUID().toString());
        userInfo.setId(getCellValue(row.getCell(0)));
        userInfo.setName(getCellValue(row.getCell(1)));
        userInfo.setAge(getCellValue(row.getCell(2)));
        userInfo.setAddress(getCellValue(row.getCell(3)));
        userInfo.setPhone(getCellValue(row.getCell(4)));
        return userInfo;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }

}
