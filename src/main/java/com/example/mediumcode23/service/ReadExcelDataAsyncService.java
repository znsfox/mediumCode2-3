package com.example.mediumcode23.service;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public interface ReadExcelDataAsyncService {
    public void readXlsCacheAsyncMybatis(XSSFSheet sheet, XSSFRow row, int start, int end, StringBuilder insertBuilder);
}

