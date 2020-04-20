package com.kyobeeUserService.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kyobeeUserService.dto.OrgCardDetailsDTO;
import com.kyobeeUserService.dto.ResponseDTO;
import com.kyobeeUserService.service.PaymentService;
import com.kyobeeUserService.util.LoggerUtil;
import com.kyobeeUserService.util.UserServiceConstants;

@CrossOrigin
@RestController
@RequestMapping("/rest/user/payment")
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@PostMapping(value = "/cardDetails", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO saveOrgCardDetails(@RequestParam Integer orgId, @RequestParam Integer customerId,
			@RequestBody OrgCardDetailsDTO orgCardDetailsDTO) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			paymentService.saveOrgCardDetails(orgId, customerId, orgCardDetailsDTO);
			responseDTO.setServiceResult("Org Card Details saved successfully");
			responseDTO.setMessage("Org Card Details saved successfully");
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);
		} catch (Exception e) {
			LoggerUtil.logError(e);
			e.printStackTrace();
			responseDTO.setServiceResult("Error while saving Org Card details.");
			responseDTO.setMessage("Error while saving Org Card details.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

}
