package com.kyobeeAccountService.service;

import java.util.List;

import com.kyobeeAccountService.dto.InvoiceDTO;

public interface PlanService {
 
	public List<InvoiceDTO> fetchInvoiceDetails(Integer orgId);

}
