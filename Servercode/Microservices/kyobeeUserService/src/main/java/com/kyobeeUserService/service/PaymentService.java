package com.kyobeeUserService.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import com.itextpdf.text.DocumentException;
import com.kyobeeUserService.dto.InvoiceDTO;
import com.kyobeeUserService.dto.OrgCardDetailsDTO;
import com.kyobeeUserService.dto.OrgPaymentDTO;
import com.kyobeeUserService.util.Exception.PromoCodeException;
import com.kyobeeUserService.util.Exception.TransactionFailureException;

public interface PaymentService {

	public Integer saveOrgCardDetails(OrgCardDetailsDTO orgCardDetailsDTO);

	public void createTransaction(OrgPaymentDTO orgPaymentDTO) throws TransactionFailureException;
	
	public Integer generateInvoice(InvoiceDTO invoiceDTO) throws DocumentException, IOException, ParseException;
	
	public BigDecimal calculateDiscount(BigDecimal amount,String promoCode) throws PromoCodeException;

}
