package com.jenson.api.controller.v1;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping
	String test(){
		return "test~~";
	}
}
