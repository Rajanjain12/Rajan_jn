package com.kyobeeUserService.dto;

import java.math.BigDecimal;

public class DiscountDTO {
	BigDecimal amount;
	String promoCode;
	InvoiceDTO invoiceDTO;
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getPromoCode() {
		return promoCode;
	}
	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}
	public InvoiceDTO getInvoiceDTO() {
		return invoiceDTO;
	}
	public void setInvoiceDTO(InvoiceDTO invoiceDTO) {
		this.invoiceDTO = invoiceDTO;
	}

}
