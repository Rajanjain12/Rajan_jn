package com.kyobeeUserService.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ORGANIZATIONSUBSCRIPTION")
public class OrganizationSubscription implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "OrganizationSubscriptionID")
	private Integer organizationSubscriptionID;

	// bi-directional many-to-one association to Organization
	@ManyToOne
	@JoinColumn(name = "OrganizationID")
	private Organization organization;

	// bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name = "CustomerID")
	private Customer customer;

	@Temporal(TemporalType.DATE)
	@Column(name = "SubscriptionDate")
	private Date subscriptionDate;

	@Column(name = "BillAmt")
	private BigDecimal billAmt;

	@Column(name = "DiscountAmt")
	private BigDecimal discountAmt;

	@Column(name = "TotalBillAmount")
	private BigDecimal TotalBillAmount;

	@Temporal(TemporalType.DATE)
	@Column(name = "StartDate")
	private Date startDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "EndDate")
	private Date endDate;

	@ManyToOne
	@JoinColumn(name = "PromotionalCodeID")
	private PromotionalCode promotionalCode;

	@Column(name = "InvoiceStatus")
	private String invoiceStatus;

	@Column(name = "InvoiceFile")
	private String invoiceFile;

	@Column(name = "CurrentActiveSubscription")
	private String currentActiveSubscription;

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

	public Integer getOrganizationSubscriptionID() {
		return organizationSubscriptionID;
	}

	public void setOrganizationSubscriptionID(Integer organizationSubscriptionID) {
		this.organizationSubscriptionID = organizationSubscriptionID;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Date getSubscriptionDate() {
		return subscriptionDate;
	}

	public void setSubscriptionDate(Date subscriptionDate) {
		this.subscriptionDate = subscriptionDate;
	}

	public BigDecimal getBillAmt() {
		return billAmt;
	}

	public void setBillAmt(BigDecimal billAmt) {
		this.billAmt = billAmt;
	}

	public BigDecimal getDiscountAmt() {
		return discountAmt;
	}

	public void setDiscountAmt(BigDecimal discountAmt) {
		this.discountAmt = discountAmt;
	}

	public BigDecimal getTotalBillAmount() {
		return TotalBillAmount;
	}

	public void setTotalBillAmount(BigDecimal totalBillAmount) {
		TotalBillAmount = totalBillAmount;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public PromotionalCode getPromotionalCode() {
		return promotionalCode;
	}

	public void setPromotionalCode(PromotionalCode promotionalCode) {
		this.promotionalCode = promotionalCode;
	}

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getInvoiceFile() {
		return invoiceFile;
	}

	public void setInvoiceFile(String invoiceFile) {
		this.invoiceFile = invoiceFile;
	}

	public String getCurrentActiveSubscription() {
		return currentActiveSubscription;
	}

	public void setCurrentActiveSubscription(String currentActiveSubscription) {
		this.currentActiveSubscription = currentActiveSubscription;
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

}
