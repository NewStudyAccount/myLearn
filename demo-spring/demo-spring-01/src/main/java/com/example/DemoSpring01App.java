package com.example;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Hello world!
 *
 */

@SpringBootApplication
public class DemoSpring01App
{
    public static void main( String[] args )
    {
        ConfigurableApplicationContext run = SpringApplication.run(DemoSpring01App.class, args);

        ConfigurableListableBeanFactory beanFactory = run.getBeanFactory();
    }
}
