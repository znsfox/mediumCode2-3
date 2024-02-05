package com.example.mediumcode23.impl;

import com.example.mediumcode23.service.ReadExcelDataAsyncService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ReadExcelDataAsyncServiceImpl implements ReadExcelDataAsyncService {
    @Override
    /**
     * 异步读取Excel数据并使用MyBatis插入数据库。
     * @param sheet Excel工作表
     * @param row Excel行
     * @param start 开始行
     * @param end 结束行
     * @param insertBuilder SQL插入语句构建器
     */
    @Async("async-executor")
    public void readXlsCacheAsyncMybatis(XSSFSheet sheet, XSSFRow row, int start, int end, StringBuilder insertBuilder) {
        // 这里应该实现将Excel数据从start行到end行的数据读取出来，
        // 并构建成数据库插入语句，然后执行数据库插入操作。
        // 以下代码为示例伪代码，需要根据实际情况进行实现。

        // 示例伪代码
        for (int rowNum = start; rowNum <= end; rowNum++) {
            row = sheet.getRow(rowNum);
            if (row != null) {
                StringBuilder singleInsert = new StringBuilder(insertBuilder);
                singleInsert.append(" VALUES (");
                // 遍历行的每个单元格，构建插入语句
                for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
                    singleInsert.append("'").append(row.getCell(cellNum).toString()).append("',");
                }
                singleInsert.deleteCharAt(singleInsert.length() - 1); // 删除最后一个逗号
                singleInsert.append(");");
                // 在这里执行数据库插入操作
                // database.execute(singleInsert.toString());
            }
        }
    }
}
