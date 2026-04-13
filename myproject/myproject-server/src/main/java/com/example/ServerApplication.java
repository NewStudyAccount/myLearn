package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = {"com.example"})
//@ComponentScan(basePackages = "com.example") // 扫描 com.example 及其所有子包
@MapperScan(basePackages = {"com.example.**.mapper"})
public class ServerApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ServerApplication.class, args);


        System.out.println("====================================");
        System.out.println("启动成功！");
        System.out.println("API 文档：http://localhost:38080/doc.html");
        System.out.println("====================================");
    }
}
