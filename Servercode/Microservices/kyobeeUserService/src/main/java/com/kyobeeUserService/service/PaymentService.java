package com.kyobeeUserService.service;

import com.kyobeeUserService.dto.OrgCardDetailsDTO;

public interface PaymentService {

	public void saveOrgCardDetails(Integer orgId,Integer customerId,OrgCardDetailsDTO orgCardDetailsDTO);
}
