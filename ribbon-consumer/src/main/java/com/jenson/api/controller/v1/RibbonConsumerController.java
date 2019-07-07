package com.jenson.api.controller.v1;

import com.jenson.app.service.RibbonConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RibbonConsumerController {

	@Autowired
	RibbonConsumerService ribbonConsumerService;

	@GetMapping("/getHello")
	public String getHelloWorldApi(){
		return ribbonConsumerService.getHelloWorldApi();
	}

}
