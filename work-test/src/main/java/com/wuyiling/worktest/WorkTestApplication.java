package com.wuyiling.worktest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wuyiling.worktest.mapper")
public class WorkTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkTestApplication.class, args);
    }

}
