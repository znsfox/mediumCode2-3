package com.example.mediumcode23.impl;

import com.example.mediumcode23.controller.ReadEasyExcelController;
import com.example.mediumcode23.service.ReadExcelCacheAsyncService;
import com.example.mediumcode23.service.ReadExcelDataAsyncService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class ReadExcelCacheAsyncServiceImpl implements ReadExcelCacheAsyncService {
    private static final Logger logger = LogManager.getLogger(ReadExcelCacheAsyncServiceImpl.class);

    private static final int STEP = 100; // 每次处理的行数


    @Autowired
    private ReadExcelDataAsyncService readExcelDataAsyncService;

    @Override
    @Async("async-executor")
    public void readXls(String filePath, String filename) throws IOException {
        @SuppressWarnings("resource")
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(filePath));
        // 读取第一个工作表
        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        // 总行数
        int maxRow = sheet.getLastRowNum();
        logger.info(filename + ".xlsx，一共" + maxRow + "行数据！");
        StringBuilder insertBuilder = new StringBuilder();

        insertBuilder.append("insert into ").append(filename).append(" ( UUID,");

        XSSFRow row = sheet.getRow(0);
        for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
            insertBuilder.append(row.getCell(i)).append(",");
        }

        insertBuilder.deleteCharAt(insertBuilder.length() - 1);
        insertBuilder.append(" ) values ( ");

        int times = maxRow / STEP + 1;
        //logger.info("将" + maxRow + "行数据分" + times + "次插入数据库！");
        for (int time = 0; time < times; time++) {
            int start = STEP * time + 1;
            int end = STEP * time + STEP;

            if (time == times - 1) {
                end = maxRow;
            }

            if(end + 1 - start > 0){
                //logger.info("第" + (time + 1) + "次插入数据库！" + "准备插入" + (end + 1 - start) + "条数据！");
                //readExcelDataAsyncService.readXlsCacheAsync(sheet, row, start, end, insertBuilder);
                readExcelDataAsyncService.readXlsCacheAsyncMybatis(sheet, row, start, end, insertBuilder);
            }
        }
    }
}
