package com.kyobeeUserService.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.kyobeeUserService.dto.PlanDetailsDTO;
import com.kyobeeUserService.dto.PromoCodeDTO;
import com.kyobeeUserService.dto.ResponseDTO;
import com.kyobeeUserService.service.PlanService;
import com.kyobeeUserService.util.LoggerUtil;
import com.kyobeeUserService.util.UserServiceConstants;
import com.kyobeeUserService.util.Exception.PromoCodeException;

@CrossOrigin
@RestController
@RequestMapping("/rest/user/plan")
public class PlanController {

	@Autowired
	private PlanService planService;

	// for fetching plan Details
	@GetMapping(value = "/planDetails", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO fetchPlanDetails(@RequestParam String country) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			PlanDetailsDTO planDetailsDTO = planService.fetchPlanDetails(country);
			responseDTO.setServiceResult(planDetailsDTO);
			responseDTO.setMessage("Plan Details fetched successfully");
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while fetching plan details.");
			responseDTO.setMessage("Error while fetching plan details.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	//For saving plan details
	@PostMapping(value = "/planDetails", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO savePlanDetails(@RequestParam Integer orgId, @RequestParam Integer customerId,
			@RequestParam List<Integer> planFeatureChargeIds) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			planService.savePlanDetails(orgId, customerId, planFeatureChargeIds);
			responseDTO.setServiceResult("Plan Details saved successfully");
			responseDTO.setMessage("Plan Details saved successfully");
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while saving plan details.");
			responseDTO.setMessage("Error while saving plan details.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

    //for saving promo code	
	@PostMapping(value = "/promoCode", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO savePromoCode(@RequestBody PromoCodeDTO promoCodeDTO) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			planService.savePromoCode(promoCodeDTO);
			responseDTO.setServiceResult("Promo code saved successfully");
			responseDTO.setMessage("Promo code saved successfully");
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);
		} catch (PromoCodeException pe) {
			LoggerUtil.logError(pe);
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
			responseDTO.setMessage(pe.getMessage());
			responseDTO.setServiceResult(pe.getMessage());
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while saving promo code");
			responseDTO.setMessage("Error while saving promo code.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}
}
