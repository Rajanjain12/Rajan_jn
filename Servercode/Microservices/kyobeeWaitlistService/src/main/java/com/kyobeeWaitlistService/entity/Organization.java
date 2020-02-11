package com.kyobeeWaitlistService.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ORGANIZATION")

public class Organization implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "OrganizationID")
	private Integer organizationID;

	@Column(name = "Active")
	private Byte active;

	@Column(name = "AdditionalComment")
	private String additionalComment;

	@Column(name = "AdsBalance")
	private Integer adsBalance;

	@Column(name = "AlcoholicDrinks")
	private Byte[] alcoholicDrinks;

	@Column(name = "AutoRenew")
	private Byte autoRenew;

	@Column(name = "CheckinSpecialAvailable")
	private Byte checkinSpecialAvailable;

	@Column(name = "CheckinSpecialNote")
	private String checkinSpecialNote;

	private String clientBase;

	@Temporal(TemporalType.DATE)
	@Column(name = "CreatedAt")
	private Date createdAt;

	@Column(name = "CreatedBy")
	private String createdBy;

	@Column(name = "DefaultLangId")
	private Integer defaultLangId;

	@Column(name = "Delivery")
	private Byte[] delivery;

	@Column(name = "Email")
	private String email;

	@Column(name = "HoursOfOperation")
	private String hoursOfOperation;

	@Column(name = "LastPaymentTrxDescription")
	private String lastPaymentTrxDescription;

	@Column(name = "LastPaymentTrxStatus")
	private String lastPaymentTrxStatus;

	@Column(name = "LiveTV")
	private Byte[] liveTV;

	private String logoFileName;

	@Column(name = "MaxParty")
	private Integer maxParty;

	@Temporal(TemporalType.DATE)
	@Column(name = "ModifiedAt")
	private Date modifiedAt;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Column(name = "NoOfSeat")
	private Integer noOfSeat;

	@Column(name = "NotifyFirstFlag")
	private String notifyFirstFlag;

	private Integer notifyUserCount;

	@Column(name = "OrganizationName")
	private String organizationName;

	@Column(name = "OrganizationPromotionalCode")
	private String organizationPromotionalCode;

	@Column(name = "OutdoorSeat")
	private Byte[] outdoorSeat;

	@Column(name = "ParkingInformation")
	private String parkingInformation;

	@Column(name = "PrimaryPhone")
	private BigInteger primaryPhone;

	@Column(name = "ReservationInfo")
	private Integer reservationInfo;

	@Column(name = "ScreensaverFileName")
	private String screensaverFileName;

	@Column(name = "ScreensaverFlag")
	private String screensaverFlag;

	@Column(name = "SecondaryPhone")
	private BigInteger secondaryPhone;

	private String smsRoute;

	private String smsRouteNo;

	private String smsSignature;

	@Column(name = "StripeCustomerID")
	private String stripeCustomerID;

	@Column(name = "TotalWaitTime")
	private Integer totalWaitTime;

	private Integer waitTime;

	// bi-directional many-to-one association to Address
	@ManyToOne
	@JoinColumn(name = "AddressID")
	private Address address;

	// bi-directional many-to-one association to Lookup
	@ManyToOne
	@JoinColumn(name = "OrganizationTypeID")
	private Lookup lookup;

	// bi-directional many-to-one association to Organizationcategory
	@OneToMany(mappedBy = "organization")
	private List<OrganizationCategory> organizationcategories;

	// bi-directional many-to-one association to Organizationlang
	@OneToMany(mappedBy = "organization")
	private List<OrganizationLang> organizationlangs;

	// bi-directional many-to-one association to Organizationtemplate
	@OneToMany(mappedBy = "organization")
	private List<OrganizationTemplate> organizationtemplates;

	// bi-directional many-to-one association to Organizationuser
	@OneToMany(mappedBy = "organization")
	private List<OrganizationUser> organizationusers;

	// bi-directional many-to-one association to Orgmarketing
	@OneToMany(mappedBy = "organization")
	private List<OrgMarketing> orgmarketings;

	public Organization() {
		// constructor
	}

	public Integer getOrganizationID() {
		return this.organizationID;
	}

	public void setOrganizationID(Integer organizationID) {
		this.organizationID = organizationID;
	}

	public Byte getActive() {
		return this.active;
	}

	public void setActive(Byte active) {
		this.active = active;
	}

	public String getAdditionalComment() {
		return this.additionalComment;
	}

	public void setAdditionalComment(String additionalComment) {
		this.additionalComment = additionalComment;
	}

	public Integer getAdsBalance() {
		return this.adsBalance;
	}

	public void setAdsBalance(Integer adsBalance) {
		this.adsBalance = adsBalance;
	}

	public Byte[] getAlcoholicDrinks() {
		return this.alcoholicDrinks;
	}

	public void setAlcoholicDrinks(Byte[] alcoholicDrinks) {
		this.alcoholicDrinks = alcoholicDrinks;
	}

	public Byte getAutoRenew() {
		return this.autoRenew;
	}

	public void setAutoRenew(Byte autoRenew) {
		this.autoRenew = autoRenew;
	}

	public Byte getCheckinSpecialAvailable() {
		return this.checkinSpecialAvailable;
	}

	public void setCheckinSpecialAvailable(Byte checkinSpecialAvailable) {
		this.checkinSpecialAvailable = checkinSpecialAvailable;
	}

	public String getCheckinSpecialNote() {
		return this.checkinSpecialNote;
	}

	public void setCheckinSpecialNote(String checkinSpecialNote) {
		this.checkinSpecialNote = checkinSpecialNote;
	}

	public String getClientBase() {
		return this.clientBase;
	}

	public void setClientBase(String clientBase) {
		this.clientBase = clientBase;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getDefaultLangId() {
		return this.defaultLangId;
	}

	public void setDefaultLangId(Integer defaultLangId) {
		this.defaultLangId = defaultLangId;
	}

	public Byte[] getDelivery() {
		return this.delivery;
	}

	public void setDelivery(Byte[] delivery) {
		this.delivery = delivery;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHoursOfOperation() {
		return this.hoursOfOperation;
	}

	public void setHoursOfOperation(String hoursOfOperation) {
		this.hoursOfOperation = hoursOfOperation;
	}

	public String getLastPaymentTrxDescription() {
		return this.lastPaymentTrxDescription;
	}

	public void setLastPaymentTrxDescription(String lastPaymentTrxDescription) {
		this.lastPaymentTrxDescription = lastPaymentTrxDescription;
	}

	public String getLastPaymentTrxStatus() {
		return this.lastPaymentTrxStatus;
	}

	public void setLastPaymentTrxStatus(String lastPaymentTrxStatus) {
		this.lastPaymentTrxStatus = lastPaymentTrxStatus;
	}

	public Byte[] getLiveTV() {
		return this.liveTV;
	}

	public void setLiveTV(Byte[] liveTV) {
		this.liveTV = liveTV;
	}

	public String getLogoFileName() {
		return this.logoFileName;
	}

	public void setLogoFileName(String logoFileName) {
		this.logoFileName = logoFileName;
	}

	public Integer getMaxParty() {
		return this.maxParty;
	}

	public void setMaxParty(Integer maxParty) {
		this.maxParty = maxParty;
	}

	public Date getModifiedAt() {
		return this.modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Integer getNoOfSeat() {
		return this.noOfSeat;
	}

	public void setNoOfSeat(Integer noOfSeat) {
		this.noOfSeat = noOfSeat;
	}

	public String getNotifyFirstFlag() {
		return this.notifyFirstFlag;
	}

	public void setNotifyFirstFlag(String notifyFirstFlag) {
		this.notifyFirstFlag = notifyFirstFlag;
	}

	public Integer getNotifyUserCount() {
		return this.notifyUserCount;
	}

	public void setNotifyUserCount(Integer notifyUserCount) {
		this.notifyUserCount = notifyUserCount;
	}

	public String getOrganizationName() {
		return this.organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getOrganizationPromotionalCode() {
		return this.organizationPromotionalCode;
	}

	public void setOrganizationPromotionalCode(String organizationPromotionalCode) {
		this.organizationPromotionalCode = organizationPromotionalCode;
	}

	public Byte[] getOutdoorSeat() {
		return this.outdoorSeat;
	}

	public void setOutdoorSeat(Byte[] outdoorSeat) {
		this.outdoorSeat = outdoorSeat;
	}

	public String getParkingInformation() {
		return this.parkingInformation;
	}

	public void setParkingInformation(String parkingInformation) {
		this.parkingInformation = parkingInformation;
	}

	public BigInteger getPrimaryPhone() {
		return this.primaryPhone;
	}

	public void setPrimaryPhone(BigInteger primaryPhone) {
		this.primaryPhone = primaryPhone;
	}

	public Integer getReservationInfo() {
		return this.reservationInfo;
	}

	public void setReservationInfo(Integer reservationInfo) {
		this.reservationInfo = reservationInfo;
	}

	public String getScreensaverFileName() {
		return this.screensaverFileName;
	}

	public void setScreensaverFileName(String screensaverFileName) {
		this.screensaverFileName = screensaverFileName;
	}

	public String getScreensaverFlag() {
		return this.screensaverFlag;
	}

	public void setScreensaverFlag(String screensaverFlag) {
		this.screensaverFlag = screensaverFlag;
	}

	public BigInteger getSecondaryPhone() {
		return this.secondaryPhone;
	}

	public void setSecondaryPhone(BigInteger secondaryPhone) {
		this.secondaryPhone = secondaryPhone;
	}

	public String getSmsRoute() {
		return this.smsRoute;
	}

	public void setSmsRoute(String smsRoute) {
		this.smsRoute = smsRoute;
	}

	public String getSmsRouteNo() {
		return this.smsRouteNo;
	}

	public void setSmsRouteNo(String smsRouteNo) {
		this.smsRouteNo = smsRouteNo;
	}

	public String getSmsSignature() {
		return this.smsSignature;
	}

	public void setSmsSignature(String smsSignature) {
		this.smsSignature = smsSignature;
	}

	public String getStripeCustomerID() {
		return this.stripeCustomerID;
	}

	public void setStripeCustomerID(String stripeCustomerID) {
		this.stripeCustomerID = stripeCustomerID;
	}

	public Integer getTotalWaitTime() {
		return this.totalWaitTime;
	}

	public void setTotalWaitTime(Integer totalWaitTime) {
		this.totalWaitTime = totalWaitTime;
	}

	public Integer getWaitTime() {
		return this.waitTime;
	}

	public void setWaitTime(Integer waitTime) {
		this.waitTime = waitTime;
	}

	public Address getAddress() {
		return this.address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Lookup getLookup() {
		return this.lookup;
	}

	public void setLookup(Lookup lookup) {
		this.lookup = lookup;
	}

	public List<OrganizationCategory> getOrganizationcategories() {
		return this.organizationcategories;
	}

	public void setOrganizationcategories1(List<OrganizationCategory> organizationcategories) {
		this.organizationcategories = organizationcategories;
	}

	public List<OrganizationLang> getOrganizationlangs() {
		return this.organizationlangs;
	}

	public void setOrganizationlangs(List<OrganizationLang> organizationlangs) {
		this.organizationlangs = organizationlangs;
	}

	public List<OrganizationTemplate> getOrganizationtemplates() {
		return this.organizationtemplates;
	}

	public void setOrganizationtemplates(List<OrganizationTemplate> organizationtemplates) {
		this.organizationtemplates = organizationtemplates;
	}

	public OrganizationTemplate addOrganizationtemplate(OrganizationTemplate organizationtemplate) {
		getOrganizationtemplates().add(organizationtemplate);
		organizationtemplate.setOrganization(this);

		return organizationtemplate;
	}

	public OrganizationTemplate removeOrganizationtemplate(OrganizationTemplate organizationtemplate) {
		getOrganizationtemplates().remove(organizationtemplate);
		organizationtemplate.setOrganization(null);

		return organizationtemplate;
	}

	public List<OrganizationUser> getOrganizationusers() {
		return this.organizationusers;
	}

	public void setOrganizationusers(List<OrganizationUser> organizationusers) {
		this.organizationusers = organizationusers;
	}

	public OrganizationUser addOrganizationusers(OrganizationUser organizationusers) {
		getOrganizationusers().add(organizationusers);
		organizationusers.setOrganization(this);

		return organizationusers;
	}

	public OrganizationUser removeOrganizationusers(OrganizationUser organizationusers) {
		getOrganizationusers().remove(organizationusers);
		organizationusers.setOrganization(null);

		return organizationusers;
	}

	public List<OrgMarketing> getOrgmarketings() {
		return this.orgmarketings;
	}

	public void setOrgmarketings(List<OrgMarketing> orgmarketings) {
		this.orgmarketings = orgmarketings;
	}

	public OrgMarketing addOrgmarketing(OrgMarketing orgmarketing) {
		getOrgmarketings().add(orgmarketing);
		orgmarketing.setOrganization(this);

		return orgmarketing;
	}

	public OrgMarketing removeOrgmarketing(OrgMarketing orgmarketing) {
		getOrgmarketings().remove(orgmarketing);
		orgmarketing.setOrganization(null);

		return orgmarketing;
	}

}
