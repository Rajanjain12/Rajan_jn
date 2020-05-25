package com.kyobeeUserService.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.kyobeeUserService.dto.OrgCardDetailsDTO;
import com.kyobeeUserService.dto.OrgPaymentDTO;
import com.kyobeeUserService.dto.OrganizationDTO;
import com.kyobeeUserService.util.Exception.TransactionFailureException;

public interface PaymentService {

	public Integer saveOrgCardDetails(Integer orgId, Integer customerId, OrgCardDetailsDTO orgCardDetailsDTO);

	public void createTransaction(OrgPaymentDTO orgPaymentDTO) throws TransactionFailureException;
	
	public Integer generateInvoice(OrganizationDTO orgDTO,List<Integer> planFeatureChargeIds,Integer orgSubscriptionId) throws DocumentException, IOException, ParseException;

}
