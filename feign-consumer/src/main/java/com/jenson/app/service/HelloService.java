package com.jenson.app.service;

import com.jenson.app.service.fallback.HelloFallBack;
import com.jenson.app.service.fallback.HelloFallBackFactory;
import com.jenson.config.FeignClientConfig;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@FeignClient(name = "hello-world",configuration = FeignClientConfig.class,fallbackFactory = HelloFallBackFactory.class)
public interface HelloService {
	@GetMapping("/query")
	List<Object> queryApiGateway();

	@GetMapping("/")
	String hello();
}
