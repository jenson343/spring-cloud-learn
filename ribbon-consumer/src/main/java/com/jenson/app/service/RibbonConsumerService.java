package com.jenson.app.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RibbonConsumerService {

	@Autowired
	RestTemplate restTemplate;

	//调用接口，在该方法上增加回调注解
	@HystrixCommand(fallbackMethod = "helloWorldApiFallBack")
	public String getHelloWorldApi(){
		return restTemplate.getForObject("http://hello-world/",String.class);
	}

	private String helloWorldApiFallBack(){
		return "error";
	}

}
