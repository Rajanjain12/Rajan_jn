package com.kyobeeUserService.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kyobeeUserService.dto.ResponseDTO;
import com.kyobeeUserService.dto.CredentialsDTO;

@CrossOrigin
@RestController
@RequestMapping("/rest/user")
public class UserController {
	
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseDTO login(@RequestBody CredentialsDTO credentialsDTO) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			System.out.println("login method");
		}
		catch (Exception e) {


		}
		return responseDTO;
	}
	

}
