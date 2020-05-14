package com.kyobeeUserService.service;

import com.kyobeeUserService.dto.OrgCardDetailsDTO;
import com.kyobeeUserService.dto.OrgPaymentDTO;

public interface PaymentService {

	public Integer saveOrgCardDetails(Integer orgId, Integer customerId, OrgCardDetailsDTO orgCardDetailsDTO);

	public void createTransaction(OrgPaymentDTO orgPaymentDTO);

}
