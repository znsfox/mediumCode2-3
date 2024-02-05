package com.example.mediumcode23;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class GenerateTestExcel {

    public static void main(String[] args) {
        int numRows = 100000; // 目标行数
        File directory = new File("d:\\test\\data");
        if (!directory.exists()){
            directory.mkdirs(); // 创建多级目录
        }

        String filePath = "d:\\test\\data\\testData.xlsx"; // 目标文件路径

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("TestData");

            // 创建标题行
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Age");
            headerRow.createCell(3).setCellValue("Address");
            headerRow.createCell(4).setCellValue("Phone");

            Random random = new Random();
            // 填充数据
            for (int i = 1; i <= numRows; i++) {
                Row row = sheet.createRow(i);
                row.createCell(0).setCellValue(UUID.randomUUID().toString());
                row.createCell(1).setCellValue("Name" + i);
                // 生成一个随机年龄在 18 到 100 之间
                row.createCell(2).setCellValue(18 + random.nextInt(83));
                row.createCell(3).setCellValue("Address" + i);
                // 生成美国格式电话号码
                String phone = String.format("(%03d) %03d-%04d", random.nextInt(1000), random.nextInt(1000), random.nextInt(10000));
                row.createCell(4).setCellValue(phone);
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
