package com.jenson.api.controller;

import com.jenson.app.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeignEurekaController {
		@Autowired
		private HelloService helloService;

		@GetMapping
		public Object queryApi(){
			return helloService.queryApiGateway();
		}

		@GetMapping("/hello")
		public String hello(){
			return helloService.hello();
		}
}
