package com.kyobeeAccountService.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kyobeeAccountService.dto.InvoiceDTO;
import com.kyobeeAccountService.dto.ResponseDTO;
import com.kyobeeAccountService.service.PlanService;
import com.kyobeeAccountService.util.AccountServiceConstants;
import com.kyobeeAccountService.util.LoggerUtil;

@CrossOrigin
@RestController
@RequestMapping("/rest/account/plan")
public class PlanController {

	@Autowired
	PlanService planService;

	// For fetching account details
	@GetMapping(value = "/invoice", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO fetchInvoiceDetails(@RequestParam Integer orgId) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			List<InvoiceDTO> invoiceList = planService.fetchInvoiceDetails(orgId);
			responseDTO.setServiceResult(invoiceList);
			responseDTO.setMessage("Invoice Details fetched successfully");
			responseDTO.setSuccess(AccountServiceConstants.SUCCESS_CODE);
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while fetching invoice details.");
			responseDTO.setMessage("Error while fetching invoice details.");
			responseDTO.setSuccess(AccountServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

}
