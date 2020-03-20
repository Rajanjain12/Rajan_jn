package com.kyobeeUtilService.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kyobeeUtilService.dto.ResponseDTO;
import com.kyobeeUtilService.service.UtilService;
import com.kyobeeUtilService.util.LoggerUtil;
import com.kyobeeUtilService.util.UtilServiceConstants;



@CrossOrigin
@RestController
@RequestMapping("/rest/util")
public class UtilController {
	
	@Autowired
	UtilService utilService;
	
	//purpose : for sending sms
	@GetMapping(value = "/sendSMS", consumes = "application/json", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO sendSMS(@RequestParam String contactNo,@RequestParam String message) {
		System.out.println("Inside util");
		ResponseDTO responseDTO = new ResponseDTO();
		try {		
			utilService.sendSMS(contactNo, message);
            responseDTO.setServiceResult("Success");
			responseDTO.setSuccess(UtilServiceConstants.SUCCESS_CODE);
		}  catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while sending sms");
			responseDTO.setMessage("Error while sending sms");
			responseDTO.setSuccess(UtilServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}
	
	//purpose : for sending email
	@GetMapping(value = "/sendEmail", consumes = "application/json", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO sendEmail(@RequestParam String toEmail ,@RequestParam String subject,@RequestParam String body) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {		
			utilService.sendEmail(toEmail, subject, body);
            responseDTO.setServiceResult("Success");
			responseDTO.setSuccess(UtilServiceConstants.SUCCESS_CODE);
		}  catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while sending mail");
			responseDTO.setMessage("Error while sending mail");
			responseDTO.setSuccess(UtilServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

}
