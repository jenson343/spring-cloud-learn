package com.jenson;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@MapperScan("com.jenson.infra.mapper")
public class HelloWorldApplication {
	public static void main(String[] args) {
		SpringApplication.run(HelloWorldApplication.class, args);
	}
}
