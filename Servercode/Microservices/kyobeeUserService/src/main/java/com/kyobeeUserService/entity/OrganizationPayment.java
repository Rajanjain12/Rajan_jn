package com.kyobeeUserService.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ORGANIZATIONPAYMENT")
public class OrganizationPayment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "OrganizationPaymentID")
	private Integer organizationPaymentID;

	// bi-directional many-to-one association to OrganizationSubscription
	@ManyToOne
	@JoinColumn(name = "InvoiceID")
	private OrganizationSubscription organizationSubscription;

	// bi-directional many-to-one association to Organization
	@ManyToOne
	@JoinColumn(name = "OrganizationID")
	private Organization organization;

	// bi-directional many-to-one association to OrganizationCardDetail
	@ManyToOne
	@JoinColumn(name = "OrganizationCardDetailID")
	private OrganizationCardDetail organizationcarddetail;

	@Column(name = "TransactionID")
	private Integer transactionID;

	@Column(name = "Amount")
	private BigDecimal amount;

	@Temporal(TemporalType.DATE)
	@Column(name = "PaymentDateTime")
	private Date paymentDateTime;

	@Column(name = "PaymentStatus")
	private String paymentStatus;

	@Column(name = "PaymentStatusReason")
	private String paymentStatusReason;

	@Column(name = "Active")
	private Byte active;

	@Column(name = "CreatedBy")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CreatedAt")
	private Date createdAt;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "ModifiedAt")
	private Date modifiedAt;

	public Integer getOrganizationPaymentID() {
		return organizationPaymentID;
	}

	public void setOrganizationPaymentID(Integer organizationPaymentID) {
		this.organizationPaymentID = organizationPaymentID;
	}

	public OrganizationSubscription getOrganizationSubscription() {
		return organizationSubscription;
	}

	public void setOrganizationSubscription(OrganizationSubscription organizationSubscription) {
		this.organizationSubscription = organizationSubscription;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public OrganizationCardDetail getOrganizationcarddetail() {
		return organizationcarddetail;
	}

	public void setOrganizationcarddetail(OrganizationCardDetail organizationcarddetail) {
		this.organizationcarddetail = organizationcarddetail;
	}

	public Integer getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(Integer transactionID) {
		this.transactionID = transactionID;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getPaymentDateTime() {
		return paymentDateTime;
	}

	public void setPaymentDateTime(Date paymentDateTime) {
		this.paymentDateTime = paymentDateTime;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPaymentStatusReason() {
		return paymentStatusReason;
	}

	public void setPaymentStatusReason(String paymentStatusReason) {
		this.paymentStatusReason = paymentStatusReason;
	}

	public Byte getActive() {
		return active;
	}

	public void setActive(Byte active) {
		this.active = active;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public OrganizationCardDetail getOrganizationcardDetail() {
		return this.organizationcarddetail;
	}

	public void setOrganizationcardDetail(OrganizationCardDetail organizationcarddetail) {
		this.organizationcarddetail = organizationcarddetail;
	}

}
