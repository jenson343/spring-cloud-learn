package com.jenson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableEurekaClient
public class SpringbootSecurityApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringbootSecurityApplication.class, args);
		System.out.println(new BCryptPasswordEncoder().encode("1234"));
	}
}