package com.kyobee.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="ORGANIZATION")
@NamedQueries({
	@NamedQuery(name=Organization.GET_ORGANIZATIONAL_DETAIL,query="Select o from Organization o  " +
			" left join fetch o.organizationAdCreditSet organizationAdCreditSet " +
			" left  join fetch o.feedbackQuestionaire feedbackQuestionaire where o.organizationId=?1"),
	@NamedQuery(name=Organization.GET_ORGANIZATIONAL_CAT_DETAIL,query="Select o from Organization o  " +
			" left join fetch o.organizationCategoryList organizationCategoryList " +
			"  where o.organizationId=?1"),
	@NamedQuery(name=Organization.GET_ORGANIZATION_BY_ID,query="Select o from Organization o  " +			
			"  where o.organizationId=:organizationId")
})
public class Organization extends BaseEntity {

	public static final String GET_ORGANIZATIONAL_DETAIL = "getOrganizationalDetail";
	public static final String GET_ORGANIZATIONAL_CAT_DETAIL = "getOrganizationalCatDetail";
	public static final String GET_ORGANIZATION_BY_ID = "getOrganizationById";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="OrganizationId")
	private Long organizationId;
	
	@Column(name="OrganizationName")
	private String organizationName;
	
	@OneToOne(fetch = FetchType.LAZY, cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name="AddressId")
	private Address address;
	
	@Column(name="OrganizationTypeId")
	private Long organizationTypeId;
	
	@Column(name="AdsBalance")
	private BigDecimal adsBalance;
	
	@Column(name="PrimaryPhone")
	private Long primaryPhone;
	
	@Column(name="SecondaryPhone")
	private Long secondaryPhone;
	
	@Column(name="Email")
	private String email;
	
	@Column(name="clientBase")
	private String clientBase;
	
	@Column(name="LastPaymentTrxStatus")
	private String lastPaymentTrxStatus;
	
	@Column(name="LastPaymentTrxDescription")
	private String lastPaymentTrxDescription;
	
	@Column(columnDefinition = "TINYINT" , name="Active" ,nullable=false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean activeFlag;
	
	@Column(columnDefinition = "TINYINT" , name="AutoRenew" ,nullable=false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean autoRenew;
	
	@Column(name="notifyUserCount")
	private Long notifyUserCount;
	
	@Column(name="logoFileName")
	private String logoFileName;
	
	@Column(name="OrganizationPromotionalCode")
	private String promotionalCode;
	
	@OneToMany(mappedBy="organization", fetch = FetchType.LAZY)
	@org.hibernate.annotations.Cascade({ org.hibernate.annotations.CascadeType.ALL,
    org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	private List<OrganizationPlanSubscription> organizationPlanSubscriptionList;

	@OneToOne(fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name="ActiveOrganizationPlanId")
	private OrganizationPlanSubscription activeOrgPlanSubscription;
	
	@OneToMany(mappedBy="organization", fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	private List<OrganizationUser> organizationUserList;

	@OneToMany(mappedBy="organization", fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	private List<OrganizationOption> organizationOptionList;

	@OneToMany(mappedBy="organization", fetch = FetchType.LAZY)
	@org.hibernate.annotations.Cascade({ org.hibernate.annotations.CascadeType.ALL,
             org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	private List<OrganizationCategory> organizationCategoryList;


	@OneToMany(mappedBy="organization", fetch = FetchType.LAZY)
	 @org.hibernate.annotations.Cascade({ org.hibernate.annotations.CascadeType.ALL,
     org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	private Set<OrganizationAdCredit> organizationAdCreditSet;

	@OneToOne(mappedBy="organization", fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	private OrganizationAdCreditScheduler  adCreditScheduler;
	

	@Column(name="StripeCustomerID")
	private String stripeCustomerId;

	@OneToOne(mappedBy="organization", fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	private FeedbackQuestionaire feedbackQuestionaire;
	
	@Column(columnDefinition = "TINYINT" , name="CheckinSpecialAvailable" ,nullable=false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean checkinSpecialAvailable;
	
	@Column(name="CheckinSpecialNote")
	private String checkinSpecialNote;
	
	@OneToMany(mappedBy="organization", fetch = FetchType.LAZY)
	 @org.hibernate.annotations.Cascade({ org.hibernate.annotations.CascadeType.ALL,
    org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	private Set<AdUsageHistoryReport> adUsageHistoryReportSet;
	
	@Column(name="waitTime")
	private Long waitTime;
	
	@Column(name="TotalWaitTime")
	private Long totalWaitTime;
	
	
	@Column(name="smsSignature")
	private String smsSignature;
	
	@Column(name="smsRoute")
	private String smsRoute;
	
	@Column(name="smsRouteNo")
	private String smsRouteNo;
	
	@Column(name="MaxParty")
	private Integer maxParty;
	
	public Integer getMaxParty() {
		return maxParty;
	}

	public void setMaxParty(Integer maxParty) {
		this.maxParty = maxParty;
	}


	public String getSmsSignature() {
		return smsSignature;
	}


	public void setSmsSignature(String smsSignature) {
		this.smsSignature = smsSignature;
	}


	public String getSmsRoute() {
		return smsRoute;
	}


	public void setSmsRoute(String smsRoute) {
		this.smsRoute = smsRoute;
	}


	
	public String getSmsRouteNo() {
		return smsRouteNo;
	}


	public void setSmsRouteNo(String smsRouteNo) {
		this.smsRouteNo = smsRouteNo;
	}


	public Long getOrganizationId() {
		return organizationId;
	}


	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}


	public String getOrganizationName() {
		return organizationName;
	}


	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}


	public Long getOrganizationTypeId() {
		return organizationTypeId;
	}


	public void setOrganizationTypeId(Long organizationTypeId) {
		this.organizationTypeId = organizationTypeId;
	}

	public Long getPrimaryPhone() {
		return primaryPhone;
	}


	public void setPrimaryPhone(Long primaryPhone) {
		this.primaryPhone = primaryPhone;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActiveFlag() {
		return activeFlag;
	}


	public void setActiveFlag(boolean activeFlag) {
		this.activeFlag = activeFlag;
	}


	public Address getAddress() {
		return address;
	}


	public void setAddress(Address address) {
		this.address = address;
	}


	public Long getSecondaryPhone() {
		return secondaryPhone;
	}


	public void setSecondaryPhone(Long secondaryPhone) {
		this.secondaryPhone = secondaryPhone;
	}


	public List<OrganizationUser> getOrganizationUserList() {
		return organizationUserList;
	}


	public void setOrganizationUserList(List<OrganizationUser> organizationUserList) {
		this.organizationUserList = organizationUserList;
	}


	public List<OrganizationOption> getOrganizationOptionList() {
		return organizationOptionList;
	}


	public void setOrganizationOptionList(
			List<OrganizationOption> organizationOptionList) {
		this.organizationOptionList = organizationOptionList;
	}


	public List<OrganizationCategory> getOrganizationCategoryList() {
		return organizationCategoryList;
	}


	public void setOrganizationCategoryList(
			List<OrganizationCategory> organizationCategoryList) {
		this.organizationCategoryList = organizationCategoryList;
	}


	public BigDecimal getAdsBalance() {
		return adsBalance;
	}


	public void setAdsBalance(BigDecimal adsBalance) {
		this.adsBalance = adsBalance;
	}

	public FeedbackQuestionaire getFeedbackQuestionaire() {
		return feedbackQuestionaire;
	}


	public void setFeedbackQuestionaire(FeedbackQuestionaire feedbackQuestionaire) {
		this.feedbackQuestionaire = feedbackQuestionaire;
	}




	public OrganizationPlanSubscription getActiveOrgPlanSubscription() {
		return activeOrgPlanSubscription;
	}


	public void setActiveOrgPlanSubscription(
			OrganizationPlanSubscription activeOrgPlanSubscription) {
		this.activeOrgPlanSubscription = activeOrgPlanSubscription;
	}


	public List<OrganizationPlanSubscription> getOrganizationPlanSubscriptionList() {
		return organizationPlanSubscriptionList;
	}


	public void setOrganizationPlanSubscriptionList(
			List<OrganizationPlanSubscription> organizationPlanSubscriptionList) {
		this.organizationPlanSubscriptionList = organizationPlanSubscriptionList;
	}


	public Set<OrganizationAdCredit> getOrganizationAdCreditSet() {
		return organizationAdCreditSet;
	}


	public void setOrganizationAdCreditSet(
			Set<OrganizationAdCredit> organizationAdCreditSet) {
		this.organizationAdCreditSet = organizationAdCreditSet;
	}


	public OrganizationAdCreditScheduler getAdCreditScheduler() {
		return adCreditScheduler;
	}


	public void setAdCreditScheduler(OrganizationAdCreditScheduler adCreditScheduler) {
		this.adCreditScheduler = adCreditScheduler;
	}


	public String getStripeCustomerId() {
		return stripeCustomerId;
	}


	public void setStripeCustomerId(String stripeCustomerId) {
		this.stripeCustomerId = stripeCustomerId;
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


	public boolean isCheckinSpecialAvailable() {
		return checkinSpecialAvailable;
	}


	public void setCheckinSpecialAvailable(boolean checkinSpecialAvailable) {
		this.checkinSpecialAvailable = checkinSpecialAvailable;
	}


	public String getCheckinSpecialNote() {
		return checkinSpecialNote;
	}


	public void setCheckinSpecialNote(String checkinSpecialNote) {
		this.checkinSpecialNote = checkinSpecialNote;
	}


	public boolean isAutoRenew() {
		return autoRenew;
	}


	public void setAutoRenew(boolean autoRenew) {
		this.autoRenew = autoRenew;
	}


	public Set<AdUsageHistoryReport> getAdUsageHistoryReportSet() {
		return adUsageHistoryReportSet;
	}


	public void setAdUsageHistoryReportSet(
			Set<AdUsageHistoryReport> adUsageHistoryReportSet) {
		this.adUsageHistoryReportSet = adUsageHistoryReportSet;
	}


	public Long getWaitTime() {
		return waitTime;
	}


	public void setWaitTime(Long waitTime) {
		this.waitTime = waitTime;
	}

	public Long getTotalWaitTime() {
		return totalWaitTime;
	}


	public void setTotalWaitTime(Long totalWaitTime) {
		this.totalWaitTime = totalWaitTime;
	}


	public Long getNotifyUserCount() {
		return notifyUserCount;
	}


	public void setNotifyUserCount(Long notifyUserCount) {
		this.notifyUserCount = notifyUserCount;
	}

	public String getLogoFileName() {
		return logoFileName;
	}

	public void setLogoFileName(String logoFileName) {
		this.logoFileName = logoFileName;
	}


	public String getClientBase() {
		return clientBase;
	}


	public void setClientBase(String clientBase) {
		this.clientBase = clientBase;
	}


	public String getPromotionalCode() {
		return promotionalCode;
	}


	public void setPromotionalCode(String promotionalCode) {
		this.promotionalCode = promotionalCode;
	}
	
	
}
