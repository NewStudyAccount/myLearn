package com.example.impl;

import com.example.utils.DataSourceUtils;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;


@Service
public class ResultSetHandlerImpl implements DataSourceUtils.ResultSetHandler {
    @Override
    public void handle(ResultSet rs) throws SQLException {
        System.out.println(rs);
        System.out.println(rs.getString("name"));
    }
}
