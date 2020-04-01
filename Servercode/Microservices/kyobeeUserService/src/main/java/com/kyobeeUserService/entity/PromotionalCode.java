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
@Table(name="PROMOTIONALCODE")
public class PromotionalCode implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "PromotionalCodeID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer promotionalCodeID;
	
	@Column(name="PromoCode")
	private String promoCode;

	@Temporal(TemporalType.DATE)
	@Column(name="ValidStartDate")
	private Date validStartDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="ValidEndDate")
	private Date validEndDate;
	
	@Column(name="CodeType")
	private String codeType;

	@Column(name="FlatAmt")
	private BigDecimal flatAmt;
	
	@Column(name="PercAmt")
	private BigDecimal percAmt;

	//bi-directional many-to-one association to Currency
	@ManyToOne
	@JoinColumn(name="CurrencyID")
	private Currency currency;
	
	@Column(name="Active")
	private byte active;
	
	@Temporal(TemporalType.DATE)
	@Column(name="CreatedAt")
	private Date createdAt;

	@Column(name="CreatedBy")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="ModifiedAt")
	private Date modifiedAt;

	@Column(name="ModifiedBy")
	private String modifiedBy;

	public Integer getPromotionalCodeID() {
		return promotionalCodeID;
	}

	public void setPromotionalCodeID(Integer promotionalCodeID) {
		this.promotionalCodeID = promotionalCodeID;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public Date getValidStartDate() {
		return validStartDate;
	}

	public void setValidStartDate(Date validStartDate) {
		this.validStartDate = validStartDate;
	}

	public Date getValidEndDate() {
		return validEndDate;
	}

	public void setValidEndDate(Date validEndDate) {
		this.validEndDate = validEndDate;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public BigDecimal getFlatAmt() {
		return flatAmt;
	}

	public void setFlatAmt(BigDecimal flatAmt) {
		this.flatAmt = flatAmt;
	}

	public BigDecimal getPercAmt() {
		return percAmt;
	}

	public void setPercAmt(BigDecimal percAmt) {
		this.percAmt = percAmt;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public byte getActive() {
		return active;
	}

	public void setActive(byte active) {
		this.active = active;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	
	

}
