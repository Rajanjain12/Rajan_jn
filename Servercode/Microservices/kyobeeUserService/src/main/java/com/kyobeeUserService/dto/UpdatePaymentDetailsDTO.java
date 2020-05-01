package com.kyobeeUserService.dto;

import java.sql.Timestamp;
import java.util.Date;

public class UpdatePaymentDetailsDTO {
	
	private String invoiceStatus;
	private String currentActiveSubscription;
	private String subscriptionStatus;
	private String vaultId;
	private String transactionId;
	private Timestamp paymentDateTime; 
	private String payemntStatusReason;
	private String paymentStatus;
	private Integer organizationSubscriptionID;
	private Integer organizationCardDetailID;
	private Integer organizationPaymentID;
	
	public String getInvoiceStatus() {
		return invoiceStatus;
	}
	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	public String getCurrentActiveSubscription() {
		return currentActiveSubscription;
	}
	public void setCurrentActiveSubscription(String currentActiveSubscription) {
		this.currentActiveSubscription = currentActiveSubscription;
	}
	public String getSubscriptionStatus() {
		return subscriptionStatus;
	}
	public void setSubscriptionStatus(String subscriptionStatus) {
		this.subscriptionStatus = subscriptionStatus;
	}
	public String getVaultId() {
		return vaultId;
	}
	public void setVaultId(String vaultId) {
		this.vaultId = vaultId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public Timestamp getPaymentDateTime() {
		return paymentDateTime;
	}
	public void setPaymentDateTime(Timestamp paymentDateTime) {
		this.paymentDateTime = paymentDateTime;
	}
	public String getPayemntStatusReason() {
		return payemntStatusReason;
	}
	public void setPayemntStatusReason(String payemntStatusReason) {
		this.payemntStatusReason = payemntStatusReason;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
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
	public Integer getOrganizationPaymentID() {
		return organizationPaymentID;
	}
	public void setOrganizationPaymentID(Integer organizationPaymentID) {
		this.organizationPaymentID = organizationPaymentID;
	}
	
}
