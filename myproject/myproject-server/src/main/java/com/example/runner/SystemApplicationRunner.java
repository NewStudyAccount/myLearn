package com.example.runner;


import com.example.factory.OssFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化 system 模块对应业务数据
 *
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SystemApplicationRunner implements ApplicationRunner {

    @Autowired
    private OssFactory ossFactory;

    @Override
    public void run(ApplicationArguments args) {
        log.info("初始化 system 模块对应业务数据");
        ossFactory.init();
        log.info("初始化 ossClient 配置完成");
    }

}
