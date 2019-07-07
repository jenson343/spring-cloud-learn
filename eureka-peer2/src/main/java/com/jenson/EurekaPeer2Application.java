package com.jenson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaPeer2Application {
	public static void main(String[] args) {
		SpringApplication.run(EurekaPeer2Application.class, args);
	}
}
