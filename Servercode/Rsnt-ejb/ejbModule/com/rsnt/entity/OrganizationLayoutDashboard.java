package com.rsnt.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.jboss.seam.annotations.Name;

@Entity
@Table(name="ORGANIZATIONLAYOUTDASHBOARD")
@Name("OrganizationLayoutDashboard")
public class OrganizationLayoutDashboard {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="OrganizationLayoutDashboardId")
	private Long organizationLayoutDashboardId;
	
	@Column(name="OrganizationId")
	private Long organizationId;
	
	@Column(name="OrganizationLayoutId")
	private Long organizationLayoutId;
	
	@Column(name="OrganizationLayoutMarkerId")
	private Long organizationLayoutMarkerId;
	
	@Column(columnDefinition = "TINYINT",name="RequestReceived", nullable=false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean requestReceived;
	
	@Column(columnDefinition = "TINYINT",name="RequestAcknowledged", nullable=false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean requestAcknowledged;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="RequestReceivedDateTime")
	private Date requestReceivedDateTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="RequestAcknowledgedDateTime")
	private Date requestAcknowledgedDateTime;
	
	@Column(name="TotalServingTime")
	private Long totalServingTime;
	
	@Column(name="AssignedWaiterID")
	private Long assignedWaiterId;

	@Column(name="DeviceID")
	private String deviceId;
	
	@Column(name="DeviceMake")
	private String deviceMake;
	
	public Long getOrganizationLayoutDashboardId() {
		return organizationLayoutDashboardId;
	}

	public void setOrganizationLayoutDashboardId(Long organizationLayoutDashboardId) {
		this.organizationLayoutDashboardId = organizationLayoutDashboardId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getOrganizationLayoutId() {
		return organizationLayoutId;
	}

	public void setOrganizationLayoutId(Long organizationLayoutId) {
		this.organizationLayoutId = organizationLayoutId;
	}

	public Long getOrganizationLayoutMarkerId() {
		return organizationLayoutMarkerId;
	}

	public void setOrganizationLayoutMarkerId(Long organizationLayoutMarkerId) {
		this.organizationLayoutMarkerId = organizationLayoutMarkerId;
	}

	public boolean isRequestReceived() {
		return requestReceived;
	}

	public void setRequestReceived(boolean requestReceived) {
		this.requestReceived = requestReceived;
	}

	public boolean isRequestAcknowledged() {
		return requestAcknowledged;
	}

	public void setRequestAcknowledged(boolean requestAcknowledged) {
		this.requestAcknowledged = requestAcknowledged;
	}

	public Date getRequestReceivedDateTime() {
		return requestReceivedDateTime;
	}

	public void setRequestReceivedDateTime(Date requestReceivedDateTime) {
		this.requestReceivedDateTime = requestReceivedDateTime;
	}

	public Date getRequestAcknowledgedDateTime() {
		return requestAcknowledgedDateTime;
	}

	public void setRequestAcknowledgedDateTime(Date requestAcknowledgedDateTime) {
		this.requestAcknowledgedDateTime = requestAcknowledgedDateTime;
	}

	public Long getTotalServingTime() {
		return totalServingTime;
	}

	public void setTotalServingTime(Long totalServingTime) {
		this.totalServingTime = totalServingTime;
	}

	public Long getAssignedWaiterId() {
		return assignedWaiterId;
	}

	public void setAssignedWaiterId(Long assignedWaiterId) {
		this.assignedWaiterId = assignedWaiterId;
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
	
	
	
	
	
	

}
