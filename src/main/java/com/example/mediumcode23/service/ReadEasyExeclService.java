package com.example.mediumcode23.service;

import com.example.mediumcode23.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface ReadEasyExeclService {


    public void saveDataBatch(List<UserInfo> userList) ;
}
