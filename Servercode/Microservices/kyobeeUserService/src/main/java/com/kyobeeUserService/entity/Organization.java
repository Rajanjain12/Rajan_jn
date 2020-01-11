package com.kyobeeUserService.entity;

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
@Table(name="ORGANIZATION")
public class Organization {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="OrganizationID")
	private Integer organizationID;

	@Column(name="Active")
	private Byte active;

	@Column(name="AdditionalComment")
	private String additionalComment;

	@Column(name="AdsBalance")
	private Integer adsBalance;

	@Column(name="AlcoholicDrinks")
	private Byte[] alcoholicDrinks;

	@Column(name="AutoRenew")
	private Byte autoRenew;

	@Column(name="CheckinSpecialAvailable")
	private Byte checkinSpecialAvailable;

	@Column(name="CheckinSpecialNote")
	private String checkinSpecialNote;

	private String clientBase;

	@Temporal(TemporalType.DATE)
	@Column(name="CreatedAt")
	private Date createdAt;

	@Column(name="CreatedBy")
	private String createdBy;

	@Column(name="DefaultLangId")
	private Integer defaultLangId;

	@Column(name="Delivery")
	private Byte[] delivery;

	@Column(name="Email")
	private String email;

	@Column(name="HoursOfOperation")
	private String hoursOfOperation;

	@Column(name="LastPaymentTrxDescription")
	private String lastPaymentTrxDescription;

	@Column(name="LastPaymentTrxStatus")
	private String lastPaymentTrxStatus;

	@Column(name="LiveTV")
	private Byte[] liveTV;

	private String logoFileName;

	@Column(name="MaxParty")
	private Integer maxParty;

	@Temporal(TemporalType.DATE)
	@Column(name="ModifiedAt")
	private Date modifiedAt;

	@Column(name="ModifiedBy")
	private String modifiedBy;

	@Column(name="NoOfSeat")
	private Integer noOfSeat;

	@Column(name="NotifyFirstFlag")
	private String notifyFirstFlag;

	private Integer notifyUserCount;

	@Column(name="OrganizationName")
	private String organizationName;

	@Column(name="OrganizationPromotionalCode")
	private String organizationPromotionalCode;

	@Column(name="OutdoorSeat")
	private Byte[] outdoorSeat;

	@Column(name="ParkingInformation")
	private String parkingInformation;

	@Column(name="PrimaryPhone")
	private BigInteger primaryPhone;

	@Column(name="ReservationInfo")
	private Integer reservationInfo;

	@Column(name="ScreensaverFileName")
	private String screensaverFileName;

	@Column(name="ScreensaverFlag")
	private String screensaverFlag;

	@Column(name="SecondaryPhone")
	private BigInteger secondaryPhone;

	private String smsRoute;

	private String smsRouteNo;

	private String smsSignature;

	@Column(name="StripeCustomerID")
	private String stripeCustomerID;

	@Column(name="TotalWaitTime")
	private Integer totalWaitTime;

	private Integer waitTime;

	//bi-directional many-to-one association to Feedback
	/*
	 * @OneToMany(mappedBy="organization") private List<Feedback> feedbacks;
	 */

	//bi-directional many-to-one association to Feedbackquestionaire
	/*
	 * @OneToMany(mappedBy="organization1") private List<Feedbackquestionaire>
	 * feedbackquestionaires1;
	 * 
	 * //bi-directional many-to-one association to Feedbackquestionaire
	 * 
	 * @OneToMany(mappedBy="organization2") private List<Feedbackquestionaire>
	 * feedbackquestionaires2;
	 */

	//bi-directional many-to-one association to Address
	@ManyToOne
	@JoinColumn(name="AddressID")
	private Address address;

	//bi-directional many-to-one association to Lookup
	@ManyToOne
	@JoinColumn(name="OrganizationTypeID")
	private Lookup lookup;

	//bi-directional many-to-one association to Organizationplansubscription
	/*
	 * @ManyToOne
	 * 
	 * @JoinColumn(name="ActiveOrganizationPlanId") private
	 * Organizationplansubscription organizationplansubscription1;
	 * 
	 * //bi-directional many-to-one association to Organizationplansubscription
	 * 
	 * @ManyToOne
	 * 
	 * @JoinColumn(name="ActiveOrganizationPlanId") private
	 * Organizationplansubscription organizationplansubscription2;
	 */

	//bi-directional many-to-one association to Address
	/*
	 * @ManyToOne
	 * 
	 * @JoinColumn(name="AddressID") private Address address2;
	 */

	//bi-directional many-to-one association to Organizationadcredit
	/*
	 * @OneToMany(mappedBy="organization1") private List<Organizationadcredit>
	 * organizationadcredits1;
	 * 
	 * //bi-directional many-to-one association to Organizationadcredit
	 * 
	 * @OneToMany(mappedBy="organization2") private List<Organizationadcredit>
	 * organizationadcredits2;
	 * 
	 * //bi-directional many-to-one association to Organizationadcreditscheduler
	 * 
	 * @OneToMany(mappedBy="organization1") private
	 * List<Organizationadcreditscheduler> organizationadcreditschedulers1;
	 * 
	 * //bi-directional many-to-one association to Organizationadcreditscheduler
	 * 
	 * @OneToMany(mappedBy="organization2") private
	 * List<Organizationadcreditscheduler> organizationadcreditschedulers2;
	 */

	//bi-directional many-to-one association to Organizationcategory
	@OneToMany(mappedBy="organization")
	private List<OrganizationCategory> organizationcategories;

	//bi-directional many-to-one association to Organizationcategory
	/*
	 * @OneToMany(mappedBy="organization2") private List<OrganizationCategory>
	 * organizationcategories2;
	 */

	//bi-directional many-to-one association to Organizationlang
	@OneToMany(mappedBy="organization")
	private List<OrganizationLang> organizationlangs;

	//bi-directional many-to-one association to Organizationlayout
	/*
	 * @OneToMany(mappedBy="organization") private List<Organizationlayout>
	 * organizationlayouts;
	 * 
	 * //bi-directional many-to-one association to Organizationlayoutdashboard
	 * 
	 * @OneToMany(mappedBy="organization") private List<Organizationlayoutdashboard>
	 * organizationlayoutdashboards;
	 * 
	 * //bi-directional many-to-one association to Organizationlayoutmarker
	 * 
	 * @OneToMany(mappedBy="organization") private List<Organizationlayoutmarker>
	 * organizationlayoutmarkers;
	 * 
	 * //bi-directional many-to-one association to Organizationlayoutoption
	 * 
	 * @OneToMany(mappedBy="organization") private List<Organizationlayoutoption>
	 * organizationlayoutoptions;
	 * 
	 * //bi-directional many-to-one association to Organizationoption
	 * 
	 * @OneToMany(mappedBy="organization1") private List<Organizationoption>
	 * organizationoptions1;
	 * 
	 * //bi-directional many-to-one association to Organizationoption
	 * 
	 * @OneToMany(mappedBy="organization2") private List<Organizationoption>
	 * organizationoptions2;
	 * 
	 * //bi-directional many-to-one association to Organizationplansubscription
	 * 
	 * @OneToMany(mappedBy="organization1") private
	 * List<Organizationplansubscription> organizationplansubscriptions1;
	 * 
	 * //bi-directional many-to-one association to Organizationplansubscription
	 * 
	 * @OneToMany(mappedBy="organization2") private
	 * List<Organizationplansubscription> organizationplansubscriptions2;
	 * 
	 * //bi-directional many-to-one association to
	 * Organizationplansubscriptioninvoice
	 * 
	 * @OneToMany(mappedBy="organization") private
	 * List<Organizationplansubscriptioninvoice>
	 * organizationplansubscriptioninvoices;
	 */

	//bi-directional many-to-one association to Organizationtemplate
	@OneToMany(mappedBy="organization")
	private List<OrganizationTemplate> organizationtemplates;

	//bi-directional many-to-one association to Organizationuser
	@OneToMany(mappedBy="organization")
	private List<OrganizationUser> organizationusers;

	//bi-directional many-to-one association to Organizationuser
	/*
	 * @OneToMany(mappedBy="organization2") private List<OrganizationUser>
	 * organizationusers2;
	 */

	//bi-directional many-to-one association to Orgmarketing
	@OneToMany(mappedBy="organization")
	private List<OrgMarketing> orgmarketings;

	public Organization() {
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

	/*
	 * public List<Feedback> getFeedbacks() { return this.feedbacks; }
	 * 
	 * public void setFeedbacks(List<Feedback> feedbacks) { this.feedbacks =
	 * feedbacks; }
	 */

	/*
	 * public Feedback addFeedback(Feedback feedback) {
	 * getFeedbacks().add(feedback); feedback.setOrganization(this);
	 * 
	 * return feedback; }
	 * 
	 * public Feedback removeFeedback(Feedback feedback) {
	 * getFeedbacks().remove(feedback); feedback.setOrganization(null);
	 * 
	 * return feedback; }
	 */

	/*
	 * public List<Feedbackquestionaire> getFeedbackquestionaires1() { return
	 * this.feedbackquestionaires1; }
	 * 
	 * public void setFeedbackquestionaires1(List<Feedbackquestionaire>
	 * feedbackquestionaires1) { this.feedbackquestionaires1 =
	 * feedbackquestionaires1; }
	 * 
	 * public Feedbackquestionaire addFeedbackquestionaires1(Feedbackquestionaire
	 * feedbackquestionaires1) {
	 * getFeedbackquestionaires1().add(feedbackquestionaires1);
	 * feedbackquestionaires1.setOrganization1(this);
	 * 
	 * return feedbackquestionaires1; }
	 * 
	 * public Feedbackquestionaire removeFeedbackquestionaires1(Feedbackquestionaire
	 * feedbackquestionaires1) {
	 * getFeedbackquestionaires1().remove(feedbackquestionaires1);
	 * feedbackquestionaires1.setOrganization1(null);
	 * 
	 * return feedbackquestionaires1; }
	 * 
	 * public List<Feedbackquestionaire> getFeedbackquestionaires2() { return
	 * this.feedbackquestionaires2; }
	 * 
	 * public void setFeedbackquestionaires2(List<Feedbackquestionaire>
	 * feedbackquestionaires2) { this.feedbackquestionaires2 =
	 * feedbackquestionaires2; }
	 */

	/*
	 * public Feedbackquestionaire addFeedbackquestionaires2(Feedbackquestionaire
	 * feedbackquestionaires2) {
	 * getFeedbackquestionaires2().add(feedbackquestionaires2);
	 * feedbackquestionaires2.setOrganization2(this);
	 * 
	 * return feedbackquestionaires2; }
	 * 
	 * public Feedbackquestionaire removeFeedbackquestionaires2(Feedbackquestionaire
	 * feedbackquestionaires2) {
	 * getFeedbackquestionaires2().remove(feedbackquestionaires2);
	 * feedbackquestionaires2.setOrganization2(null);
	 * 
	 * return feedbackquestionaires2; }
	 */
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

	/*
	 * public Organizationplansubscription getOrganizationplansubscription1() {
	 * return this.organizationplansubscription1; }
	 * 
	 * public void setOrganizationplansubscription1(Organizationplansubscription
	 * organizationplansubscription1) { this.organizationplansubscription1 =
	 * organizationplansubscription1; }
	 * 
	 * public Organizationplansubscription getOrganizationplansubscription2() {
	 * return this.organizationplansubscription2; }
	 * 
	 * public void setOrganizationplansubscription2(Organizationplansubscription
	 * organizationplansubscription2) { this.organizationplansubscription2 =
	 * organizationplansubscription2; }
	 */

	/*
	 * public Address getAddress2() { return this.address2; }
	 * 
	 * public void setAddress2(Address address2) { this.address2 = address2; }
	 */

	/*
	 * public List<Organizationadcredit> getOrganizationadcredits1() { return
	 * this.organizationadcredits1; }
	 * 
	 * public void setOrganizationadcredits1(List<Organizationadcredit>
	 * organizationadcredits1) { this.organizationadcredits1 =
	 * organizationadcredits1; }
	 * 
	 * public Organizationadcredit addOrganizationadcredits1(Organizationadcredit
	 * organizationadcredits1) {
	 * getOrganizationadcredits1().add(organizationadcredits1);
	 * organizationadcredits1.setOrganization1(this);
	 * 
	 * return organizationadcredits1; }
	 * 
	 * public Organizationadcredit removeOrganizationadcredits1(Organizationadcredit
	 * organizationadcredits1) {
	 * getOrganizationadcredits1().remove(organizationadcredits1);
	 * organizationadcredits1.setOrganization1(null);
	 * 
	 * return organizationadcredits1; }
	 */

	/*
	 * public List<Organizationadcredit> getOrganizationadcredits2() { return
	 * this.organizationadcredits2; }
	 * 
	 * public void setOrganizationadcredits2(List<Organizationadcredit>
	 * organizationadcredits2) { this.organizationadcredits2 =
	 * organizationadcredits2; }
	 * 
	 * public Organizationadcredit addOrganizationadcredits2(Organizationadcredit
	 * organizationadcredits2) {
	 * getOrganizationadcredits2().add(organizationadcredits2);
	 * organizationadcredits2.setOrganization2(this);
	 * 
	 * return organizationadcredits2; }
	 * 
	 * public Organizationadcredit removeOrganizationadcredits2(Organizationadcredit
	 * organizationadcredits2) {
	 * getOrganizationadcredits2().remove(organizationadcredits2);
	 * organizationadcredits2.setOrganization2(null);
	 * 
	 * return organizationadcredits2; }
	 * 
	 * public List<Organizationadcreditscheduler>
	 * getOrganizationadcreditschedulers1() { return
	 * this.organizationadcreditschedulers1; }
	 * 
	 * public void
	 * setOrganizationadcreditschedulers1(List<Organizationadcreditscheduler>
	 * organizationadcreditschedulers1) { this.organizationadcreditschedulers1 =
	 * organizationadcreditschedulers1; }
	 * 
	 * public Organizationadcreditscheduler
	 * addOrganizationadcreditschedulers1(Organizationadcreditscheduler
	 * organizationadcreditschedulers1) {
	 * getOrganizationadcreditschedulers1().add(organizationadcreditschedulers1);
	 * organizationadcreditschedulers1.setOrganization1(this);
	 * 
	 * return organizationadcreditschedulers1; }
	 * 
	 * public Organizationadcreditscheduler
	 * removeOrganizationadcreditschedulers1(Organizationadcreditscheduler
	 * organizationadcreditschedulers1) {
	 * getOrganizationadcreditschedulers1().remove(organizationadcreditschedulers1);
	 * organizationadcreditschedulers1.setOrganization1(null);
	 * 
	 * return organizationadcreditschedulers1; }
	 * 
	 * public List<Organizationadcreditscheduler>
	 * getOrganizationadcreditschedulers2() { return
	 * this.organizationadcreditschedulers2; }
	 * 
	 * public void
	 * setOrganizationadcreditschedulers2(List<Organizationadcreditscheduler>
	 * organizationadcreditschedulers2) { this.organizationadcreditschedulers2 =
	 * organizationadcreditschedulers2; }
	 * 
	 * public Organizationadcreditscheduler
	 * addOrganizationadcreditschedulers2(Organizationadcreditscheduler
	 * organizationadcreditschedulers2) {
	 * getOrganizationadcreditschedulers2().add(organizationadcreditschedulers2);
	 * organizationadcreditschedulers2.setOrganization2(this);
	 * 
	 * return organizationadcreditschedulers2; }
	 * 
	 * public Organizationadcreditscheduler
	 * removeOrganizationadcreditschedulers2(Organizationadcreditscheduler
	 * organizationadcreditschedulers2) {
	 * getOrganizationadcreditschedulers2().remove(organizationadcreditschedulers2);
	 * organizationadcreditschedulers2.setOrganization2(null);
	 * 
	 * return organizationadcreditschedulers2; }
	 */
	public List<OrganizationCategory> getOrganizationcategories() {
		return this.organizationcategories;
	}

	public void setOrganizationcategories1(List<OrganizationCategory> organizationcategories) {
		this.organizationcategories = organizationcategories;
	}

	/*
	 * public Organizationcategory addOrganizationcategories1(Organizationcategory
	 * organizationcategories1) {
	 * getOrganizationcategories1().add(organizationcategories1);
	 * organizationcategories1.setOrganization1(this);
	 * 
	 * return organizationcategories1; }
	 * 
	 * public Organizationcategory
	 * removeOrganizationcategories1(Organizationcategory organizationcategories1) {
	 * getOrganizationcategories1().remove(organizationcategories1);
	 * organizationcategories1.setOrganization1(null);
	 * 
	 * return organizationcategories1; }
	 */
	/*
	 * public List<OrganizationCategory> getOrganizationcategories2() { return
	 * this.organizationcategories2; }
	 * 
	 * public void setOrganizationcategories2(List<OrganizationCategory>
	 * organizationcategories2) { this.organizationcategories2 =
	 * organizationcategories2; }
	 */

	/*
	 * public Organizationcategory addOrganizationcategories2(Organizationcategory
	 * organizationcategories2) {
	 * getOrganizationcategories2().add(organizationcategories2);
	 * organizationcategories2.setOrganization2(this);
	 * 
	 * return organizationcategories2; }
	 * 
	 * public Organizationcategory
	 * removeOrganizationcategories2(Organizationcategory organizationcategories2) {
	 * getOrganizationcategories2().remove(organizationcategories2);
	 * organizationcategories2.setOrganization2(null);
	 * 
	 * return organizationcategories2; }
	 */
	public List<OrganizationLang> getOrganizationlangs() {
		return this.organizationlangs;
	}

	public void setOrganizationlangs(List<OrganizationLang> organizationlangs) {
		this.organizationlangs = organizationlangs;
	}

	/*
	 * public Organizationlang addOrganizationlang(Organizationlang
	 * organizationlang) { getOrganizationlangs().add(organizationlang);
	 * organizationlang.setOrganization(this);
	 * 
	 * return organizationlang; }
	 * 
	 * public Organizationlang removeOrganizationlang(Organizationlang
	 * organizationlang) { getOrganizationlangs().remove(organizationlang);
	 * organizationlang.setOrganization(null);
	 * 
	 * return organizationlang; }
	 * 
	 * public List<Organizationlayout> getOrganizationlayouts() { return
	 * this.organizationlayouts; }
	 * 
	 * public void setOrganizationlayouts(List<Organizationlayout>
	 * organizationlayouts) { this.organizationlayouts = organizationlayouts; }
	 * 
	 * public Organizationlayout addOrganizationlayout(Organizationlayout
	 * organizationlayout) { getOrganizationlayouts().add(organizationlayout);
	 * organizationlayout.setOrganization(this);
	 * 
	 * return organizationlayout; }
	 * 
	 * public Organizationlayout removeOrganizationlayout(Organizationlayout
	 * organizationlayout) { getOrganizationlayouts().remove(organizationlayout);
	 * organizationlayout.setOrganization(null);
	 * 
	 * return organizationlayout; }
	 * 
	 * public List<Organizationlayoutdashboard> getOrganizationlayoutdashboards() {
	 * return this.organizationlayoutdashboards; }
	 * 
	 * public void setOrganizationlayoutdashboards(List<Organizationlayoutdashboard>
	 * organizationlayoutdashboards) { this.organizationlayoutdashboards =
	 * organizationlayoutdashboards; }
	 */
	/*
	 * public Organizationlayoutdashboard
	 * addOrganizationlayoutdashboard(Organizationlayoutdashboard
	 * organizationlayoutdashboard) {
	 * getOrganizationlayoutdashboards().add(organizationlayoutdashboard);
	 * organizationlayoutdashboard.setOrganization(this);
	 * 
	 * return organizationlayoutdashboard; }
	 * 
	 * public Organizationlayoutdashboard
	 * removeOrganizationlayoutdashboard(Organizationlayoutdashboard
	 * organizationlayoutdashboard) {
	 * getOrganizationlayoutdashboards().remove(organizationlayoutdashboard);
	 * organizationlayoutdashboard.setOrganization(null);
	 * 
	 * return organizationlayoutdashboard; }
	 * 
	 * public List<Organizationlayoutmarker> getOrganizationlayoutmarkers() { return
	 * this.organizationlayoutmarkers; }
	 * 
	 * public void setOrganizationlayoutmarkers(List<Organizationlayoutmarker>
	 * organizationlayoutmarkers) { this.organizationlayoutmarkers =
	 * organizationlayoutmarkers; }
	 * 
	 * public Organizationlayoutmarker
	 * addOrganizationlayoutmarker(Organizationlayoutmarker
	 * organizationlayoutmarker) {
	 * getOrganizationlayoutmarkers().add(organizationlayoutmarker);
	 * organizationlayoutmarker.setOrganization(this);
	 * 
	 * return organizationlayoutmarker; }
	 * 
	 * public Organizationlayoutmarker
	 * removeOrganizationlayoutmarker(Organizationlayoutmarker
	 * organizationlayoutmarker) {
	 * getOrganizationlayoutmarkers().remove(organizationlayoutmarker);
	 * organizationlayoutmarker.setOrganization(null);
	 * 
	 * return organizationlayoutmarker; }
	 * 
	 * public List<Organizationlayoutoption> getOrganizationlayoutoptions() { return
	 * this.organizationlayoutoptions; }
	 * 
	 * public void setOrganizationlayoutoptions(List<Organizationlayoutoption>
	 * organizationlayoutoptions) { this.organizationlayoutoptions =
	 * organizationlayoutoptions; }
	 * 
	 * public Organizationlayoutoption
	 * addOrganizationlayoutoption(Organizationlayoutoption
	 * organizationlayoutoption) {
	 * getOrganizationlayoutoptions().add(organizationlayoutoption);
	 * organizationlayoutoption.setOrganization(this);
	 * 
	 * return organizationlayoutoption; }
	 * 
	 * public Organizationlayoutoption
	 * removeOrganizationlayoutoption(Organizationlayoutoption
	 * organizationlayoutoption) {
	 * getOrganizationlayoutoptions().remove(organizationlayoutoption);
	 * organizationlayoutoption.setOrganization(null);
	 * 
	 * return organizationlayoutoption; }
	 * 
	 * public List<Organizationoption> getOrganizationoptions1() { return
	 * this.organizationoptions1; }
	 * 
	 * public void setOrganizationoptions1(List<Organizationoption>
	 * organizationoptions1) { this.organizationoptions1 = organizationoptions1; }
	 * 
	 * public Organizationoption addOrganizationoptions1(Organizationoption
	 * organizationoptions1) { getOrganizationoptions1().add(organizationoptions1);
	 * organizationoptions1.setOrganization1(this);
	 * 
	 * return organizationoptions1; }
	 * 
	 * public Organizationoption removeOrganizationoptions1(Organizationoption
	 * organizationoptions1) {
	 * getOrganizationoptions1().remove(organizationoptions1);
	 * organizationoptions1.setOrganization1(null);
	 * 
	 * return organizationoptions1; }
	 * 
	 * public List<Organizationoption> getOrganizationoptions2() { return
	 * this.organizationoptions2; }
	 * 
	 * public void setOrganizationoptions2(List<Organizationoption>
	 * organizationoptions2) { this.organizationoptions2 = organizationoptions2; }
	 * 
	 * public Organizationoption addOrganizationoptions2(Organizationoption
	 * organizationoptions2) { getOrganizationoptions2().add(organizationoptions2);
	 * organizationoptions2.setOrganization2(this);
	 * 
	 * return organizationoptions2; }
	 * 
	 * public Organizationoption removeOrganizationoptions2(Organizationoption
	 * organizationoptions2) {
	 * getOrganizationoptions2().remove(organizationoptions2);
	 * organizationoptions2.setOrganization2(null);
	 * 
	 * return organizationoptions2; }
	 * 
	 * public List<Organizationplansubscription> getOrganizationplansubscriptions1()
	 * { return this.organizationplansubscriptions1; }
	 * 
	 * public void
	 * setOrganizationplansubscriptions1(List<Organizationplansubscription>
	 * organizationplansubscriptions1) { this.organizationplansubscriptions1 =
	 * organizationplansubscriptions1; }
	 * 
	 * public Organizationplansubscription
	 * addOrganizationplansubscriptions1(Organizationplansubscription
	 * organizationplansubscriptions1) {
	 * getOrganizationplansubscriptions1().add(organizationplansubscriptions1);
	 * organizationplansubscriptions1.setOrganization1(this);
	 * 
	 * return organizationplansubscriptions1; }
	 * 
	 * public Organizationplansubscription
	 * removeOrganizationplansubscriptions1(Organizationplansubscription
	 * organizationplansubscriptions1) {
	 * getOrganizationplansubscriptions1().remove(organizationplansubscriptions1);
	 * organizationplansubscriptions1.setOrganization1(null);
	 * 
	 * return organizationplansubscriptions1; }
	 * 
	 * public List<Organizationplansubscription> getOrganizationplansubscriptions2()
	 * { return this.organizationplansubscriptions2; }
	 * 
	 * public void
	 * setOrganizationplansubscriptions2(List<Organizationplansubscription>
	 * organizationplansubscriptions2) { this.organizationplansubscriptions2 =
	 * organizationplansubscriptions2; }
	 * 
	 * public Organizationplansubscription
	 * addOrganizationplansubscriptions2(Organizationplansubscription
	 * organizationplansubscriptions2) {
	 * getOrganizationplansubscriptions2().add(organizationplansubscriptions2);
	 * organizationplansubscriptions2.setOrganization2(this);
	 * 
	 * return organizationplansubscriptions2; }
	 * 
	 * public Organizationplansubscription
	 * removeOrganizationplansubscriptions2(Organizationplansubscription
	 * organizationplansubscriptions2) {
	 * getOrganizationplansubscriptions2().remove(organizationplansubscriptions2);
	 * organizationplansubscriptions2.setOrganization2(null);
	 * 
	 * return organizationplansubscriptions2; }
	 * 
	 * public List<Organizationplansubscriptioninvoice>
	 * getOrganizationplansubscriptioninvoices() { return
	 * this.organizationplansubscriptioninvoices; }
	 * 
	 * public void setOrganizationplansubscriptioninvoices(List<
	 * Organizationplansubscriptioninvoice> organizationplansubscriptioninvoices) {
	 * this.organizationplansubscriptioninvoices =
	 * organizationplansubscriptioninvoices; }
	 * 
	 * public Organizationplansubscriptioninvoice
	 * addOrganizationplansubscriptioninvoice(Organizationplansubscriptioninvoice
	 * organizationplansubscriptioninvoice) {
	 * getOrganizationplansubscriptioninvoices().add(
	 * organizationplansubscriptioninvoice);
	 * organizationplansubscriptioninvoice.setOrganization(this);
	 * 
	 * return organizationplansubscriptioninvoice; }
	 * 
	 * public Organizationplansubscriptioninvoice
	 * removeOrganizationplansubscriptioninvoice(Organizationplansubscriptioninvoice
	 * organizationplansubscriptioninvoice) {
	 * getOrganizationplansubscriptioninvoices().remove(
	 * organizationplansubscriptioninvoice);
	 * organizationplansubscriptioninvoice.setOrganization(null);
	 * 
	 * return organizationplansubscriptioninvoice; }
	 */
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

	/*
	 * public List<OrganizationUser> getOrganizationusers() { return
	 * this.organizationusers; }
	 * 
	 * public void setOrganizationusers(List<OrganizationUser> organizationusers) {
	 * this.organizationusers = organizationusers; }
	 * 
	 * public OrganizationUser addOrganizationusers(OrganizationUser
	 * organizationusers) { getOrganizationusers().add(organizationusers);
	 * organizationusers.setOrganization(this);
	 * 
	 * return organizationusers; }
	 * 
	 * public OrganizationUser removeOrganizationusers(OrganizationUser
	 * organizationusers) { getOrganizationusers().remove(organizationusers);
	 * organizationusers.setOrganization(null);
	 * 
	 * return organizationusers; }
	 */

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
