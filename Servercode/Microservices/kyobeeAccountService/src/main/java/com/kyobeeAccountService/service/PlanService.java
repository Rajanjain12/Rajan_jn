package com.kyobeeAccountService.service;

import java.util.List;

import com.kyobeeAccountService.dto.InvoiceDTO;
import com.kyobeeAccountService.dto.SubscribedPlanDetailsDTO;

public interface PlanService {

	public List<InvoiceDTO> fetchInvoiceDetails(Integer orgId);

	public List<SubscribedPlanDetailsDTO> fetchPlanDetails(Integer orgId);
	
	public List<Integer> fetchChangedPlanDetails(Integer orgId);
	
	public Integer changePlanSubcription(Integer orgId, Integer customerId, List<Integer> planFeatureChargeIds);

}
