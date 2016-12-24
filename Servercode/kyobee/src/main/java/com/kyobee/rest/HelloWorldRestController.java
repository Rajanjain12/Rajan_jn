package com.kyobee.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web/hello")
public class HelloWorldRestController {

	@RequestMapping(value = "/greetings", method = RequestMethod.GET, produces = "application/json")
	public String hello(){
		return "Hello... Good Morning";
	}
}
