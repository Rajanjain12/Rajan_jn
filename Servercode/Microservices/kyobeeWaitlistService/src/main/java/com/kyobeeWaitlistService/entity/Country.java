package com.kyobeeWaitlistService.entity;

import java.io.Serializable;
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
@Table(name = "COUNTRY")
public class Country implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CountryID")
	private Integer countryID;

	@Column(name = "CountryName")
	private String countryName;

	@Column(name = "ISOCode")
	private String ISOCode;

	@ManyToOne
	@JoinColumn(name = "CurrencyID")
	private Currency currency;

	@Column(name = "Active")
	private Byte active;

	@Temporal(TemporalType.DATE)
	@Column(name = "CreatedAt")
	private Date createdAt;

	@Column(name = "CreatedBy")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "ModifiedAt")
	private Date modifiedAt;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	public Integer getCountryID() {
		return countryID;
	}

	public void setCountryID(Integer countryID) {
		this.countryID = countryID;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getISOCode() {
		return ISOCode;
	}

	public void setISOCode(String iSOCode) {
		ISOCode = iSOCode;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Byte getActive() {
		return active;
	}

	public void setActive(Byte active) {
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
