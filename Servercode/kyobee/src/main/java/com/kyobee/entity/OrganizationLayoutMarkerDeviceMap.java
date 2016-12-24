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
@Entity(name="OrganizationLayoutMarkerDeviceMap")
@Table(name="ORGANIZATIONLAYOUTMARKERDEVICEMAP")

public class OrganizationLayoutMarkerDeviceMap{
	
	
	@Column(name="OrganizationLayoutMarkerDeviceID")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long organizationLayoutMarkerDeviceId;
	
	
	@Column(name="OrganizationLayoutMarkerID")
	private Long organizationLayoutMarkerId;
	
	@Column(name="DeviceID")
	private String deviceId;
	
	@Column(name="DeviceMake")
	private String deviceMake;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="RequestDate")
	private Date requestDate;

	public Long getOrganizationLayoutMarkerDeviceId() {
		return organizationLayoutMarkerDeviceId;
	}

	public void setOrganizationLayoutMarkerDeviceId(
			Long organizationLayoutMarkerDeviceId) {
		this.organizationLayoutMarkerDeviceId = organizationLayoutMarkerDeviceId;
	}

	public Long getOrganizationLayoutMarkerId() {
		return organizationLayoutMarkerId;
	}

	public void setOrganizationLayoutMarkerId(Long organizationLayoutMarkerId) {
		this.organizationLayoutMarkerId = organizationLayoutMarkerId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceMake() {
		return deviceMake;
	}

	public void setDeviceMake(String deviceMake) {
		this.deviceMake = deviceMake;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	
	
}
