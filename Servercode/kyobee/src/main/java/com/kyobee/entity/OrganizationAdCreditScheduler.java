package com.kyobee.entity;

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

@Entity
@Table(name="ORGANIZATIONADCREDITSCHEDULER")
public class OrganizationAdCreditScheduler extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="OrganizationAdCreditSchedulerId")
	private Long organizationAdCreditSchedulerId;
	
	@JoinColumn(name="OrganizationId")
	@ManyToOne(fetch = FetchType.LAZY,cascade={})
	private Organization organization;

	@Column(name="NoOfAdsCredited")
	private Long noOfAdsCredited;
	
	@Column(name="UnitId")
	private Long unitId;
	
	@Column(name="creditEndDate")
	private Date creditEndDate;
	
	@Column(name="nextScheduledDate")
	private Date nextScheduledDate;

	@Column(name="OrganizationPlanId")
	private Long organizationPlanId;
	
	
	public Long getOrganizationAdCreditSchedulerId() {
		return organizationAdCreditSchedulerId;
	}

	public void setOrganizationAdCreditSchedulerId(
			Long organizationAdCreditSchedulerId) {
		this.organizationAdCreditSchedulerId = organizationAdCreditSchedulerId;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Long getNoOfAdsCredited() {
		return noOfAdsCredited;
	}

	public void setNoOfAdsCredited(Long noOfAdsCredited) {
		this.noOfAdsCredited = noOfAdsCredited;
	}

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}


	public Date getNextScheduledDate() {
		return nextScheduledDate;
	}

	public void setNextScheduledDate(Date nextScheduledDate) {
		this.nextScheduledDate = nextScheduledDate;
	}

	public Date getCreditEndDate() {
		return creditEndDate;
	}

	public void setCreditEndDate(Date creditEndDate) {
		this.creditEndDate = creditEndDate;
	}

	public Long getOrganizationPlanId() {
		return organizationPlanId;
	}

	public void setOrganizationPlanId(Long organizationPlanId) {
		this.organizationPlanId = organizationPlanId;
	}

	
}