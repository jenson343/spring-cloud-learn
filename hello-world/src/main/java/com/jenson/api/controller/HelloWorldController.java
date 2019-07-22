package com.jenson.api.controller;

import com.jenson.api.controller.dto.ApiGatewayDTO;
import com.jenson.infra.mapper.ApiGatewayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloWorldController {
	@Value("${server.port}")
	String port;

	@Value("${value}")
	String testValue;

	@Autowired
	private ApiGatewayMapper apiGatewayMapper;

	@GetMapping("/")
	public String hello() {
		return "hello world：" + port + " testValue:" + testValue;
	}

	@GetMapping("/query")
	public List<ApiGatewayDTO> queryApiGateway() {
		return apiGatewayMapper.queryApiGateway();
	}
}
