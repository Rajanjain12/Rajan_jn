package com.kyobeeUserService.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kyobeeUserService.dto.OrgCardDetailsDTO;
import com.kyobeeUserService.dto.OrgPaymentDTO;
import com.kyobeeUserService.dto.OrganizationDTO;
import com.kyobeeUserService.dto.ResponseDTO;
import com.kyobeeUserService.service.PaymentService;
import com.kyobeeUserService.util.LoggerUtil;
import com.kyobeeUserService.util.UserServiceConstants;
import com.kyobeeUserService.util.Exception.TransactionFailureException;

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
			Integer orgCardDetailId = paymentService.saveOrgCardDetails(orgId, customerId, orgCardDetailsDTO);
			responseDTO.setServiceResult(orgCardDetailId);
			responseDTO.setMessage("Org Card Details saved successfully");
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while saving Org Card details.");
			responseDTO.setMessage("Error while saving Org Card details.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// API for payment transaction
	@PostMapping(value = "/createTransaction", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO createTransaction(@RequestBody OrgPaymentDTO orgPaymentDTO) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			paymentService.createTransaction(orgPaymentDTO);
			responseDTO.setServiceResult("Successfully done payment");
			responseDTO.setMessage("Successfully done payment");
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);
		} catch (TransactionFailureException ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("Error during processing payment transaction");
			responseDTO.setMessage("Error during processing payment transaction.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
			ex.printStackTrace();
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while payment");
			responseDTO.setMessage("Error while payment.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
			e.printStackTrace();
		}
		return responseDTO;
	}

	// API for generating invoice pdf and storing in aws s3
	@PostMapping(value = "/generateInvoice", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO generateInvoice(@RequestParam List<Integer> featureChargeIds, @RequestParam Integer orgSubscriptionId, @RequestBody OrganizationDTO orgDTO) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			LoggerUtil.logInfo("feature id list:"+featureChargeIds);
			LoggerUtil.logInfo("org subc id:"+orgSubscriptionId);
			LoggerUtil.logInfo("Organization name controller:"+orgDTO.getOrganizationName());
			LoggerUtil.logInfo("Organization add controller:"+orgDTO.getAddressDTO().getAddressLineOne()+orgDTO.getAddressDTO().getCity()+orgDTO.getAddressDTO().getState()+orgDTO.getAddressDTO().getZipcode());
			paymentService.generateInvoice(orgDTO, featureChargeIds,orgSubscriptionId);
			responseDTO.setServiceResult("Invoice pdf generated successfully");
			responseDTO.setMessage("Invoice pdf generated successfullyt");
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while generating pdf invoice.");
			responseDTO.setMessage("Error while generating pdf invoice.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
			e.printStackTrace();
		}
		return responseDTO;
	}

}
