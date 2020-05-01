package com.kyobeeUserService.dto;

public class OrgPaymentDTO {

	private Integer organizationSubscriptionID;
	private Integer organizationCardDetailID;
	private Integer invoiceID;
	private Integer orgID;
	private Integer amount;
	private String paymentNonce;
	
	public Integer getOrganizationSubscriptionID() {
		return organizationSubscriptionID;
	}
	public void setOrganizationSubscriptionID(Integer organizationSubscriptionID) {
		this.organizationSubscriptionID = organizationSubscriptionID;
	}
	public Integer getOrganizationCardDetailID() {
		return organizationCardDetailID;
	}
	public void setOrganizationCardDetailID(Integer organizationCardDetailID) {
		this.organizationCardDetailID = organizationCardDetailID;
	}
	public Integer getInvoiceID() {
		return invoiceID;
	}
	public void setInvoiceID(Integer invoiceID) {
		this.invoiceID = invoiceID;
	}
	public Integer getOrgID() {
		return orgID;
	}
	public void setOrgID(Integer orgID) {
		this.orgID = orgID;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getPaymentNonce() {
		return paymentNonce;
	}
	public void setPaymentNonce(String paymentNonce) {
		this.paymentNonce = paymentNonce;
	}
	
}
