package com.zhy.educms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
//TODO 实现banner后台管理前端
@SpringBootApplication
@EnableDiscoveryClient //nacos注册
@ComponentScan(basePackages = {"com.zhy"})
@MapperScan("com.zhy.educms.mapper")
public class CmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsApplication.class, args);
    }
}
