package com.rsnt.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.jboss.seam.annotations.Name;

@Entity
@Table(name="ADUSAGEHISTORYREPORT")
@Name("AdUsageHistoryReport")
public class AdUsageHistoryReport {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="AdUsageHistID")
	private Long adUsageHistId;
	
	@JoinColumn(name="OrganizationId")
	@ManyToOne(fetch = FetchType.LAZY,cascade={})
	private Organization organization;
	
	@Column(name="ActivityDate")
	private Date activityDate;
	
	@Column(name="ActivityRef")
	private String activityRef;
	
	@Column(name="ActivityDesc")
	private String activityDesc;
	
	@Column(name="AdsCredit")
	private Long adsCredit;
	
	@Column(name="AdsBalance")
	private Long adsBalance;
	
	@Column(name="Amount")
	private BigDecimal amount;
	
	@Column(name="Notes")
	private String notes;
	
	@Column(name="ActivitySeq")
	private Long activitySeq;

	public Long getAdUsageHistId() {
		return adUsageHistId;
	}

	public void setAdUsageHistId(Long adUsageHistId) {
		this.adUsageHistId = adUsageHistId;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Date getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}

	public String getActivityRef() {
		return activityRef;
	}

	public void setActivityRef(String activityRef) {
		this.activityRef = activityRef;
	}

	public String getActivityDesc() {
		return activityDesc;
	}

	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
	}

	public Long getAdsCredit() {
		return adsCredit;
	}

	public void setAdsCredit(Long adsCredit) {
		this.adsCredit = adsCredit;
	}

	public Long getAdsBalance() {
		return adsBalance;
	}

	public void setAdsBalance(Long adsBalance) {
		this.adsBalance = adsBalance;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Long getActivitySeq() {
		return activitySeq;
	}

	public void setActivitySeq(Long activitySeq) {
		this.activitySeq = activitySeq;
	}
}
