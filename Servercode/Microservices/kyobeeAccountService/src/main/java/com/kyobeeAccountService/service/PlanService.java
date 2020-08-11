package com.kyobeeAccountService.service;

import java.util.List;

import com.kyobeeAccountService.dto.InvoiceDTO;
import com.kyobeeAccountService.dto.SubscribedPlanDetailsDTO;

public interface PlanService {

	public List<InvoiceDTO> fetchInvoiceDetails(Integer orgId);

	public List<SubscribedPlanDetailsDTO> fetchPlanDetails(Integer orgId);

}
