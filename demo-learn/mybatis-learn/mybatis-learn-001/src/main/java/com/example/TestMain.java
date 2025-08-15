package com.example;

import com.example.domain.User;
import com.example.utils.DataSourceUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.sql.SQLException;
import java.util.Map;


@ComponentScan("com.example")
public class TestMain {

    public static void main(String[] args) throws SQLException {

        ApplicationContext context = new AnnotationConfigApplicationContext(TestMain.class);
        // 通过 ApplicationContext 获取 DataSourceUtils bean
        DataSourceUtils dataSourceUtils = context.getBean(DataSourceUtils.class);

        String sql = "select * from user where id = :id";
        Map<String,Object> queryParams = Map.of("id", 1);
        User user = dataSourceUtils.executeQueryOne(sql, User.class, queryParams);

        System.out.println(user);



    }
}
