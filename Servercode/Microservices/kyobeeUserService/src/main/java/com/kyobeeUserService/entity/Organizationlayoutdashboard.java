package com.kyobeeUserService.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


public class Organizationlayoutdashboard {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="OrganizationLayoutDashboardID")
	private int organizationLayoutDashboardID;

	@Column(name="AlertID")
	private int alertID;

	@Column(name="DeviceId")
	private String deviceId;

	@Column(name="DeviceMake")
	private String deviceMake;

	@Column(name="RequestAcknowledged")
	private byte requestAcknowledged;

	@Column(name="REQUESTACKNOWLEDGEDDATETIME")
	private Timestamp requestacknowledgeddatetime;

	@Column(name="RequestReceived")
	private byte requestReceived;

	@Column(name="REQUESTRECEIVEDDATETIME")
	private Timestamp requestreceiveddatetime;

	@Column(name="TotalServingTime")
	private int totalServingTime;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="AssignedWaiterID")
	private User user;

	//bi-directional many-to-one association to Organization
	@ManyToOne
	@JoinColumn(name="OrganizationID")
	private Organization organization;

	//bi-directional many-to-one association to Organizationlayout
	@ManyToOne
	@JoinColumn(name="OrganizationLayoutID")
	private Organizationlayout organizationlayout;

	//bi-directional many-to-one association to Organizationlayoutmarker
	@ManyToOne
	@JoinColumn(name="OrganizationLayoutMarkerID")
	private Organizationlayoutmarker organizationlayoutmarker;

	public Organizationlayoutdashboard() {
	}

	public int getOrganizationLayoutDashboardID() {
		return this.organizationLayoutDashboardID;
	}

	public void setOrganizationLayoutDashboardID(int organizationLayoutDashboardID) {
		this.organizationLayoutDashboardID = organizationLayoutDashboardID;
	}

	public int getAlertID() {
		return this.alertID;
	}

	public void setAlertID(int alertID) {
		this.alertID = alertID;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceMake() {
		return this.deviceMake;
	}

	public void setDeviceMake(String deviceMake) {
		this.deviceMake = deviceMake;
	}

	public byte getRequestAcknowledged() {
		return this.requestAcknowledged;
	}

	public void setRequestAcknowledged(byte requestAcknowledged) {
		this.requestAcknowledged = requestAcknowledged;
	}

	public Timestamp getRequestacknowledgeddatetime() {
		return this.requestacknowledgeddatetime;
	}

	public void setRequestacknowledgeddatetime(Timestamp requestacknowledgeddatetime) {
		this.requestacknowledgeddatetime = requestacknowledgeddatetime;
	}

	public byte getRequestReceived() {
		return this.requestReceived;
	}

	public void setRequestReceived(byte requestReceived) {
		this.requestReceived = requestReceived;
	}

	public Timestamp getRequestreceiveddatetime() {
		return this.requestreceiveddatetime;
	}

	public void setRequestreceiveddatetime(Timestamp requestreceiveddatetime) {
		this.requestreceiveddatetime = requestreceiveddatetime;
	}

	public int getTotalServingTime() {
		return this.totalServingTime;
	}

	public void setTotalServingTime(int totalServingTime) {
		this.totalServingTime = totalServingTime;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Organizationlayout getOrganizationlayout() {
		return this.organizationlayout;
	}

	public void setOrganizationlayout(Organizationlayout organizationlayout) {
		this.organizationlayout = organizationlayout;
	}

	public Organizationlayoutmarker getOrganizationlayoutmarker() {
		return this.organizationlayoutmarker;
	}

	public void setOrganizationlayoutmarker(Organizationlayoutmarker organizationlayoutmarker) {
		this.organizationlayoutmarker = organizationlayoutmarker;
	}

}
