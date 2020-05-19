package com.kyobeeUserService.service;

import com.kyobeeUserService.dto.OrgCardDetailsDTO;
import com.kyobeeUserService.dto.OrgPaymentDTO;
import com.kyobeeUserService.util.Exception.TransactionFailureException;

public interface PaymentService {

	public Integer saveOrgCardDetails(Integer orgId, Integer customerId, OrgCardDetailsDTO orgCardDetailsDTO);

	public void createTransaction(OrgPaymentDTO orgPaymentDTO) throws TransactionFailureException;

}
