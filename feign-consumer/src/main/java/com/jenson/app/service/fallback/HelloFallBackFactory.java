package com.jenson.app.service.fallback;

import com.jenson.app.service.HelloService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HelloFallBackFactory implements FallbackFactory<HelloService> {

	private static final Logger LOGGER = LoggerFactory.getLogger(HelloFallBackFactory.class);

	@Override
	public HelloService create(Throwable cause) {

		HelloFallBackFactory.LOGGER.info("fallback; reason was: {}", cause.getMessage());

		return new HelloClientWithFallBack(){

			@Override
			public List<Object> queryApiGateway() {
				List<Object> objectList = (new ArrayList<>());
				objectList.add("error api from factory");
				return (objectList);
			}

			@Override
			public String hello() {
				return "error hello from factory";
			}
		};
	}
}
