package com.kyobee.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name="AdsCreditAnalytics")
@Table(name="ADSCREDITANALYTICS")
public class AdsCreditAnalytics extends BaseEntity{
	
	@Column(name="AdsCreditAnalyticsID")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long adsCreditAnalyticsId;
	
	@Column(name="OrganizationLayoutMarkerID")
	private Long organizationLayoutMarkerId;

	@Column(name="OrganizationID")
	private Long organizationId;
	
	@Column(name="AdsID")
	private Long adsID;
	
	@Column(name="ViewDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date viewDate;

	public Long getAdsCreditAnalyticsId() {
		return adsCreditAnalyticsId;
	}

	public void setAdsCreditAnalyticsId(Long adsCreditAnalyticsId) {
		this.adsCreditAnalyticsId = adsCreditAnalyticsId;
	}

	public Long getOrganizationLayoutMarkerId() {
		return organizationLayoutMarkerId;
	}

	public void setOrganizationLayoutMarkerId(Long organizationLayoutMarkerId) {
		this.organizationLayoutMarkerId = organizationLayoutMarkerId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getAdsID() {
		return adsID;
	}

	public void setAdsID(Long adsID) {
		this.adsID = adsID;
	}

	public Date getViewDate() {
		return viewDate;
	}

	public void setViewDate(Date viewDate) {
		this.viewDate = viewDate;
	}
	
	
	
	


}
