package com.kyobeeAccountService.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ORGANIZATIONCARDDETAIL")
public class OrganizationCardDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "OrganizationCardDetailID")
	private Integer organizationCardDetailID;

	@Column(name = "CardNo")
	private Integer cardNo;

	@Column(name = "CardType")
	private String cardType;

	@Column(name = "CardName")
	private String cardName;

	@Column(name = "VaultID")
	private String vaultID;

	// bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name = "CustomerID")
	private Customer customer;

	// bi-directional many-to-one association to Organization
	@ManyToOne
	@JoinColumn(name = "OrganizationID")
	private Organization organization;

	@Column(name = "Active")
	private byte active;

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

	// bi-directional many-to-one association to Organizationpayment
	@OneToMany(mappedBy = "organizationcarddetail")
	private List<OrganizationPayment> organizationpayments;

	public Integer getOrganizationCardDetailID() {
		return organizationCardDetailID;
	}

	public void setOrganizationCardDetailID(Integer organizationCardDetailID) {
		this.organizationCardDetailID = organizationCardDetailID;
	}

	public Integer getCardNo() {
		return cardNo;
	}

	public void setCardNo(Integer cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getVaultID() {
		return vaultID;
	}

	public void setVaultID(String vaultID) {
		this.vaultID = vaultID;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public byte getActive() {
		return active;
	}

	public void setActive(byte active) {
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

	public List<OrganizationPayment> getOrganizationpayments() {
		return this.organizationpayments;
	}

	public void setOrganizationpayments(List<OrganizationPayment> organizationpayments) {
		this.organizationpayments = organizationpayments;
	}

	public OrganizationPayment addOrganizationpayment(OrganizationPayment organizationpayment) {
		getOrganizationpayments().add(organizationpayment);
		organizationpayment.setOrganizationcardDetail(this);

		return organizationpayment;
	}

	public OrganizationPayment removeOrganizationpayment(OrganizationPayment organizationpayment) {
		getOrganizationpayments().remove(organizationpayment);
		organizationpayment.setOrganizationcardDetail(null);

		return organizationpayment;
	}
}
