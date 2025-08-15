package com.example.Config;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionManager;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;

import java.sql.Connection;

public class MybatisConfig {

    public void ss(){

        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory();

        SqlSessionManager sqlSessionManager = SqlSessionManager.newInstance(sqlSessionFactory);


    }

}
