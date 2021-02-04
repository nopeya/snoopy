package com.nopeya.fooapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.nopeya.fooapi.mapper")
@SpringBootApplication
public class FooApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FooApiApplication.class, args);
    }

}
