package com.example;

import com.example.impl.ResultSetHandlerImpl;
import com.example.utils.DataSourceUtils;

import java.sql.SQLException;

public class TestMain {

    public static void main(String[] args) throws SQLException {
        DataSourceUtils dataSourceUtils = new DataSourceUtils();

        ResultSetHandlerImpl resultSetHandler = new ResultSetHandlerImpl();
        dataSourceUtils.executeQuery("select * from user",resultSetHandler );
    }
}
