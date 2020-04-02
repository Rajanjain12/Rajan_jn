package com.kyobeeUserService.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
@Table(name = "ORGANIZATION")
public class Organization implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "OrganizationID")
	private Integer organizationID;

	@Column(name = "OrganizationName")
	private String organizationName;

	@ManyToOne
	@JoinColumn(name = "CustomerID")
	private Customer customer;

	// bi-directional many-to-one association to Lookup
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "OrganizationTypeID", referencedColumnName = "TypeID")
	private OrganizationType organizationType;

	// bi-directional many-to-one association to Address
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "AddressID")
	private Address address;

	@Column(name = "AdsBalance")
	private Integer adsBalance;

	@Column(name = "PrimaryPhone")
	private BigInteger primaryPhone;

	@Column(name = "SecondaryPhone")
	private BigInteger secondaryPhone;

	@Column(name = "Email")
	private String email;

	@Column(name = "HoursOfOperation")
	private String hoursOfOperation;

	@Column(name = "OutdoorSeat")
	private Byte[] outdoorSeat;

	@Column(name = "LiveTV")
	private Byte[] liveTV;

	@Column(name = "Delivery")
	private Byte[] delivery;

	@Column(name = "AlcoholicDrinks")
	private Byte[] alcoholicDrinks;

	@Column(name = "AdditionalComment")
	private String additionalComment;

	@Column(name = "ParkingInformation")
	private String parkingInformation;

	@Column(name = "NoOfSeat")
	private Integer noOfSeat;

	@Column(name = "ReservationInfo")
	private Integer reservationInfo;

	@Column(name = "AutoRenew")
	private Byte autoRenew;

	@Column(name = "LastPaymentTrxStatus")
	private String lastPaymentTrxStatus;

	@Column(name = "LastPaymentTrxDescription")
	private String lastPaymentTrxDescription;

	@Column(name = "StripeCustomerID")
	private String stripeCustomerID;

	@Column(name = "DefaultLangId")
	private Integer defaultLangId;

	@Column(name = "WaitTime")
	private Integer waitTime;

	@Column(name = "NotifyUserCount")
	private Integer notifyUserCount;

	@Column(name = "ClientBase")
	private String clientBase;

	@Column(name = "LogoFileName")
	private String logoFileName;

	@Column(name = "OrganizationPromotionalCode")
	private String organizationPromotionalCode;

	@Column(name = "NotifyFirstFlag")
	private String notifyFirstFlag;

	@Column(name = "TotalWaitTime")
	private Integer totalWaitTime;

	@Column(name = "SmsSignature")
	private String smsSignature;

	@Column(name = "MaxParty")
	private Integer maxParty;

	@Column(name = "PplBifurcation")
	private String pplBifurcation;

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

	// bi-directional many-to-one association to Organizationcategory
	@OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
	private List<OrganizationCategory> organizationcategories;

	// bi-directional many-to-one association to Organizationlang
	@OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
	private List<OrganizationLang> organizationlangs;

	// bi-directional many-to-one association to Organizationtemplate
	@OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
	private List<OrganizationTemplate> organizationtemplates;

	// bi-directional many-to-one association to Organizationuser
	@OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
	private List<OrganizationUser> organizationusers;

	/*
	 * @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL) private
	 * List<OrganizationPlanSubscription> organizationPlanSubscriptionList;
	 */

	public Organization() {
		// constructor
	}

	
	public Integer getOrganizationID() {
		return organizationID;
	}


	public void setOrganizationID(Integer organizationID) {
		this.organizationID = organizationID;
	}


	public String getOrganizationName() {
		return organizationName;
	}


	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}


	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


	public OrganizationType getOrganizationType() {
		return organizationType;
	}


	public void setOrganizationType(OrganizationType organizationType) {
		this.organizationType = organizationType;
	}


	public Address getAddress() {
		return address;
	}


	public void setAddress(Address address) {
		this.address = address;
	}


	public Integer getAdsBalance() {
		return adsBalance;
	}


	public void setAdsBalance(Integer adsBalance) {
		this.adsBalance = adsBalance;
	}


	public BigInteger getPrimaryPhone() {
		return primaryPhone;
	}


	public void setPrimaryPhone(BigInteger primaryPhone) {
		this.primaryPhone = primaryPhone;
	}


	public BigInteger getSecondaryPhone() {
		return secondaryPhone;
	}


	public void setSecondaryPhone(BigInteger secondaryPhone) {
		this.secondaryPhone = secondaryPhone;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getHoursOfOperation() {
		return hoursOfOperation;
	}


	public void setHoursOfOperation(String hoursOfOperation) {
		this.hoursOfOperation = hoursOfOperation;
	}


	public Byte[] getOutdoorSeat() {
		return outdoorSeat;
	}


	public void setOutdoorSeat(Byte[] outdoorSeat) {
		this.outdoorSeat = outdoorSeat;
	}


	public Byte[] getLiveTV() {
		return liveTV;
	}


	public void setLiveTV(Byte[] liveTV) {
		this.liveTV = liveTV;
	}


	public Byte[] getDelivery() {
		return delivery;
	}


	public void setDelivery(Byte[] delivery) {
		this.delivery = delivery;
	}


	public Byte[] getAlcoholicDrinks() {
		return alcoholicDrinks;
	}


	public void setAlcoholicDrinks(Byte[] alcoholicDrinks) {
		this.alcoholicDrinks = alcoholicDrinks;
	}


	public String getAdditionalComment() {
		return additionalComment;
	}


	public void setAdditionalComment(String additionalComment) {
		this.additionalComment = additionalComment;
	}


	public String getParkingInformation() {
		return parkingInformation;
	}


	public void setParkingInformation(String parkingInformation) {
		this.parkingInformation = parkingInformation;
	}


	public Integer getNoOfSeat() {
		return noOfSeat;
	}


	public void setNoOfSeat(Integer noOfSeat) {
		this.noOfSeat = noOfSeat;
	}


	public Integer getReservationInfo() {
		return reservationInfo;
	}


	public void setReservationInfo(Integer reservationInfo) {
		this.reservationInfo = reservationInfo;
	}


	public Byte getAutoRenew() {
		return autoRenew;
	}


	public void setAutoRenew(Byte autoRenew) {
		this.autoRenew = autoRenew;
	}


	public String getLastPaymentTrxStatus() {
		return lastPaymentTrxStatus;
	}


	public void setLastPaymentTrxStatus(String lastPaymentTrxStatus) {
		this.lastPaymentTrxStatus = lastPaymentTrxStatus;
	}


	public String getLastPaymentTrxDescription() {
		return lastPaymentTrxDescription;
	}


	public void setLastPaymentTrxDescription(String lastPaymentTrxDescription) {
		this.lastPaymentTrxDescription = lastPaymentTrxDescription;
	}


	public String getStripeCustomerID() {
		return stripeCustomerID;
	}


	public void setStripeCustomerID(String stripeCustomerID) {
		this.stripeCustomerID = stripeCustomerID;
	}


	public Integer getDefaultLangId() {
		return defaultLangId;
	}


	public void setDefaultLangId(Integer defaultLangId) {
		this.defaultLangId = defaultLangId;
	}


	public Integer getWaitTime() {
		return waitTime;
	}


	public void setWaitTime(Integer waitTime) {
		this.waitTime = waitTime;
	}


	public Integer getNotifyUserCount() {
		return notifyUserCount;
	}


	public void setNotifyUserCount(Integer notifyUserCount) {
		this.notifyUserCount = notifyUserCount;
	}


	public String getClientBase() {
		return clientBase;
	}


	public void setClientBase(String clientBase) {
		this.clientBase = clientBase;
	}


	public String getLogoFileName() {
		return logoFileName;
	}


	public void setLogoFileName(String logoFileName) {
		this.logoFileName = logoFileName;
	}


	public String getOrganizationPromotionalCode() {
		return organizationPromotionalCode;
	}


	public void setOrganizationPromotionalCode(String organizationPromotionalCode) {
		this.organizationPromotionalCode = organizationPromotionalCode;
	}


	public String getNotifyFirstFlag() {
		return notifyFirstFlag;
	}


	public void setNotifyFirstFlag(String notifyFirstFlag) {
		this.notifyFirstFlag = notifyFirstFlag;
	}


	public Integer getTotalWaitTime() {
		return totalWaitTime;
	}


	public void setTotalWaitTime(Integer totalWaitTime) {
		this.totalWaitTime = totalWaitTime;
	}


	public String getSmsSignature() {
		return smsSignature;
	}


	public void setSmsSignature(String smsSignature) {
		this.smsSignature = smsSignature;
	}


	public Integer getMaxParty() {
		return maxParty;
	}


	public void setMaxParty(Integer maxParty) {
		this.maxParty = maxParty;
	}


	public String getPplBifurcation() {
		return pplBifurcation;
	}


	public void setPplBifurcation(String pplBifurcation) {
		this.pplBifurcation = pplBifurcation;
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


	public List<OrganizationCategory> getOrganizationcategories() {
		return this.organizationcategories;
	}

	public void setOrganizationcategories(List<OrganizationCategory> organizationcategories) {
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

}
