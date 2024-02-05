package com.example.mediumcode23.util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class JdbcUtil {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcUtil(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 执行DML操作：INSERT, UPDATE, DELETE
    public static int executeDML(String sql, Object... args) {
        return jdbcTemplate.update(sql, args);
    }

    public static String executeSelect(String sql, String num) {
    }

    // 执行批量DML操作
    public int[] batchUpdate(List<String> sqls) {
        String[] sqlArray = new String[sqls.size()];
        sqls.toArray(sqlArray);
        return jdbcTemplate.batchUpdate(sqlArray);
    }

    // 执行SELECT查询并返回一个值
    public static String executeSelect(String sql, Object[] args, Class<String> requiredType) {
        return jdbcTemplate.queryForObject(sql, args, requiredType);
    }
}
