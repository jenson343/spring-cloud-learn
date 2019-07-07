package com.jenson.app.service.fallback;

import com.jenson.app.service.HelloService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HelloFallBack implements HelloService {
	@Override
	public List<Object> queryApiGateway() {
		List<Object> objectList = (new ArrayList<>());
		objectList.add("error api");
		return (objectList);
	}

	@Override
	public String hello() {
		return "error hello";
	}
}
