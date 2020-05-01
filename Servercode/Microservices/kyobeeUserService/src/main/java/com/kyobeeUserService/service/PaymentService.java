package com.kyobeeUserService.service;

import com.kyobeeUserService.dto.OrgCardDetailsDTO;
import com.kyobeeUserService.dto.OrgPaymentDTO;

public interface PaymentService {

	public void saveOrgCardDetails(Integer orgId, Integer customerId, OrgCardDetailsDTO orgCardDetailsDTO);

	public void createTransaction(OrgPaymentDTO orgPaymentDTO);

}
