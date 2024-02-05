package com.example.mediumcode23.impl;

import com.example.mediumcode23.pojo.UserInfo;
import com.example.mediumcode23.service.ReadEasyExeclService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.example.mediumcode23.util.JdbcUtil;

import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReadEasyExeclServiceImpl implements ReadEasyExeclService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void saveDataBatch(List<UserInfo> userList) {
        String sql = "INSERT INTO user_info (uuid, id, name, age, address, phone) VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                UserInfo user = userList.get(i);
                ps.setString(1, user.getUuid());
                ps.setString(2, user.getId());
                ps.setString(3, user.getName());
                ps.setString(4, user.getAge()); // Assuming age is stored as String
                ps.setString(5, user.getAddress());
                ps.setString(6, user.getPhone());
            }

            @Override
            public int getBatchSize() {
                return userList.size();
            }
        });

        // Clear the list after batch update
        userList.clear();
    }

    private void readXls(String filePath, String filename) throws Exception {
        @SuppressWarnings("resource")
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(filePath));
        // 读取第一个工作表
        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        // 总行数
        int maxRow = sheet.getLastRowNum();

        StringBuilder insertBuilder = new StringBuilder();

        insertBuilder.append("insert into ").append(filename).append(" ( UUID,");

        XSSFRow row = sheet.getRow(0);
        for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
            insertBuilder.append(row.getCell(i)).append(",");
        }

        insertBuilder.deleteCharAt(insertBuilder.length() - 1);
        insertBuilder.append(" ) values ( ");

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <= maxRow; i++) {
            XSSFRow xssfRow = sheet.getRow(i);
            String id = "";
            String name = "";
            for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                if (j == 0) {
                    id = xssfRow.getCell(j) + "";
                } else if (j == 1) {
                    name = xssfRow.getCell(j) + "";
                }
            }

            boolean flag = isExisted(id, name);
            if (!flag) {
                stringBuilder.append(insertBuilder);
                stringBuilder.append('\'').append(uuid()).append('\'').append(",");
                for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                    stringBuilder.append('\'').append(value).append('\'').append(",");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                stringBuilder.append(" )").append("\n");
            }
        }

        List<String> collect = Arrays.stream(stringBuilder.toString().split("\n")).collect(Collectors.toList());
        int sum = JdbcUtil.executeDML(collect.toString());
    }

    private static boolean isExisted(String id, String name) {
        String static_TABLE = "user_info";
        String sql = "select count(1) as num from " + static_TABLE + " where ID = '" + id + "' and NAME = '" + name + "'";
        String num = JdbcUtil.executeSelect(sql, "num");
        return Integer.valueOf(num) > 0;
    }

    private static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
