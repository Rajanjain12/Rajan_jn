package com.kyobeeUserService.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="LOOKUP")
public class Lookup implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="LookupID")
	private Integer lookupID;

	@Column(name="Code")
	private String code;

	@Temporal(TemporalType.DATE)
	@Column(name="CreatedAt")
	private Date createdAt;

	@Column(name="CreatedBy")
	private String createdBy;

	@Column(name="Description")
	private String description;

	@Column(name="Filter")
	private Integer filter;

	@Temporal(TemporalType.DATE)
	@Column(name="ModifiedAt")
	private Date modifiedAt;

	@Column(name="ModifiedBy")
	private String modifiedBy;

	@Column(name="Name")
	private String name;

	@Column(name="Position")
	private Integer position;

	//bi-directional many-to-one association to Adsimage
	/*
	 * @OneToMany(mappedBy="lookup") private List<Adsimage> adsimages;
	 * 
	 * //bi-directional many-to-one association to Feedbackquestionairedetail
	 * 
	 * @OneToMany(mappedBy="lookup") private List<Feedbackquestionairedetail>
	 * feedbackquestionairedetails;
	 * 
	 * //bi-directional many-to-one association to
	 * Feedbackquestionaireresponsedetail
	 * 
	 * @OneToMany(mappedBy="lookup") private
	 * List<Feedbackquestionaireresponsedetail> feedbackquestionaireresponsedetails;
	 */

	//bi-directional many-to-one association to Lookuptype
	@ManyToOne
	@JoinColumn(name="LookupTypeID")
	private LookupType lookuptype;

	//bi-directional many-to-one association to Organization
	@OneToMany(mappedBy="lookup")
	private List<Organization> organizations;

	//bi-directional many-to-one association to Organizationadcredit
	/*
	 * @OneToMany(mappedBy="lookup") private List<Organizationadcredit>
	 * organizationadcredits;
	 * 
	 * //bi-directional many-to-one association to Organizationadcreditscheduler
	 * 
	 * @OneToMany(mappedBy="lookup") private List<Organizationadcreditscheduler>
	 * organizationadcreditschedulers;
	 */

	//bi-directional many-to-one association to Organizationcategory
	@OneToMany(mappedBy="lookup")
	private List<OrganizationCategory> organizationcategories;

	//bi-directional many-to-one association to Organizationplansubscription
	/*
	 * @OneToMany(mappedBy="lookup1") private List<Organizationplansubscription>
	 * organizationplansubscriptions1;
	 * 
	 * //bi-directional many-to-one association to Organizationplansubscription
	 * 
	 * @OneToMany(mappedBy="lookup2") private List<Organizationplansubscription>
	 * organizationplansubscriptions2;
	 */

	//bi-directional many-to-one association to Planfeature
	/*
	 * @OneToMany(mappedBy="lookup") private List<Planfeature> planfeatures;
	 * 
	 * //bi-directional many-to-one association to Planprice
	 * 
	 * @OneToMany(mappedBy="lookup1") private List<Planprice> planprices1;
	 * 
	 * //bi-directional many-to-one association to Planprice
	 * 
	 * @OneToMany(mappedBy="lookup2") private List<Planprice> planprices2;
	 * 
	 * //bi-directional many-to-one association to Planprice
	 * 
	 * @OneToMany(mappedBy="lookup3") private List<Planprice> planprices3;
	 * 
	 * //bi-directional many-to-one association to Planprice
	 * 
	 * @OneToMany(mappedBy="lookup4") private List<Planprice> planprices4;
	 */

	//bi-directional many-to-one association to Userrole
	@OneToMany(mappedBy="lookup")
	private List<Userrole> userroles;

	public Lookup() {
	}

	public Integer getLookupID() {
		return this.lookupID;
	}

	public void setLookupID(Integer lookupID) {
		this.lookupID = lookupID;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getFilter() {
		return this.filter;
	}

	public void setFilter(Integer filter) {
		this.filter = filter;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPosition() {
		return this.position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	/*
	 * public List<Adsimage> getAdsimages() { return this.adsimages; }
	 * 
	 * public void setAdsimages(List<Adsimage> adsimages) { this.adsimages =
	 * adsimages; }
	 * 
	 * public Adsimage addAdsimage(Adsimage adsimage) {
	 * getAdsimages().add(adsimage); adsimage.setLookup(this);
	 * 
	 * return adsimage; }
	 * 
	 * public Adsimage removeAdsimage(Adsimage adsimage) {
	 * getAdsimages().remove(adsimage); adsimage.setLookup(null);
	 * 
	 * return adsimage; }
	 * 
	 * public List<Feedbackquestionairedetail> getFeedbackquestionairedetails() {
	 * return this.feedbackquestionairedetails; }
	 * 
	 * public void setFeedbackquestionairedetails(List<Feedbackquestionairedetail>
	 * feedbackquestionairedetails) { this.feedbackquestionairedetails =
	 * feedbackquestionairedetails; }
	 * 
	 * public Feedbackquestionairedetail
	 * addFeedbackquestionairedetail(Feedbackquestionairedetail
	 * feedbackquestionairedetail) {
	 * getFeedbackquestionairedetails().add(feedbackquestionairedetail);
	 * feedbackquestionairedetail.setLookup(this);
	 * 
	 * return feedbackquestionairedetail; }
	 * 
	 * public Feedbackquestionairedetail
	 * removeFeedbackquestionairedetail(Feedbackquestionairedetail
	 * feedbackquestionairedetail) {
	 * getFeedbackquestionairedetails().remove(feedbackquestionairedetail);
	 * feedbackquestionairedetail.setLookup(null);
	 * 
	 * return feedbackquestionairedetail; }
	 * 
	 * public List<Feedbackquestionaireresponsedetail>
	 * getFeedbackquestionaireresponsedetails() { return
	 * this.feedbackquestionaireresponsedetails; }
	 * 
	 * public void setFeedbackquestionaireresponsedetails(List<
	 * Feedbackquestionaireresponsedetail> feedbackquestionaireresponsedetails) {
	 * this.feedbackquestionaireresponsedetails =
	 * feedbackquestionaireresponsedetails; }
	 * 
	 * public Feedbackquestionaireresponsedetail
	 * addFeedbackquestionaireresponsedetail(Feedbackquestionaireresponsedetail
	 * feedbackquestionaireresponsedetail) {
	 * getFeedbackquestionaireresponsedetails().add(
	 * feedbackquestionaireresponsedetail);
	 * feedbackquestionaireresponsedetail.setLookup(this);
	 * 
	 * return feedbackquestionaireresponsedetail; }
	 * 
	 * public Feedbackquestionaireresponsedetail
	 * removeFeedbackquestionaireresponsedetail(Feedbackquestionaireresponsedetail
	 * feedbackquestionaireresponsedetail) {
	 * getFeedbackquestionaireresponsedetails().remove(
	 * feedbackquestionaireresponsedetail);
	 * feedbackquestionaireresponsedetail.setLookup(null);
	 * 
	 * return feedbackquestionaireresponsedetail; }
	 */
	public LookupType getLookuptype() {
		return this.lookuptype;
	}

	public void setLookuptype(LookupType lookuptype) {
		this.lookuptype = lookuptype;
	}

	public List<Organization> getOrganizations() {
		return this.organizations;
	}

	public void setOrganizations(List<Organization> organizations) {
		this.organizations = organizations;
	}

	public Organization addOrganization(Organization organization) {
		getOrganizations().add(organization);
		organization.setLookup(this);

		return organization;
	}

	public Organization removeOrganization(Organization organization) {
		getOrganizations().remove(organization);
		organization.setLookup(null);

		return organization;
	}

	/*
	 * public List<Organizationadcredit> getOrganizationadcredits() { return
	 * this.organizationadcredits; }
	 * 
	 * public void setOrganizationadcredits(List<Organizationadcredit>
	 * organizationadcredits) { this.organizationadcredits = organizationadcredits;
	 * }
	 */

	/*
	 * public Organizationadcredit addOrganizationadcredit(Organizationadcredit
	 * organizationadcredit) { getOrganizationadcredits().add(organizationadcredit);
	 * organizationadcredit.setLookup(this);
	 * 
	 * return organizationadcredit; }
	 * 
	 * public Organizationadcredit removeOrganizationadcredit(Organizationadcredit
	 * organizationadcredit) {
	 * getOrganizationadcredits().remove(organizationadcredit);
	 * organizationadcredit.setLookup(null);
	 * 
	 * return organizationadcredit; }
	 */

	/*
	 * public List<Organizationadcreditscheduler>
	 * getOrganizationadcreditschedulers() { return
	 * this.organizationadcreditschedulers; }
	 * 
	 * public void
	 * setOrganizationadcreditschedulers(List<Organizationadcreditscheduler>
	 * organizationadcreditschedulers) { this.organizationadcreditschedulers =
	 * organizationadcreditschedulers; }
	 */

	/*
	 * public Organizationadcreditscheduler
	 * addOrganizationadcreditscheduler(Organizationadcreditscheduler
	 * organizationadcreditscheduler) {
	 * getOrganizationadcreditschedulers().add(organizationadcreditscheduler);
	 * organizationadcreditscheduler.setLookup(this);
	 * 
	 * return organizationadcreditscheduler; }
	 * 
	 * public Organizationadcreditscheduler
	 * removeOrganizationadcreditscheduler(Organizationadcreditscheduler
	 * organizationadcreditscheduler) {
	 * getOrganizationadcreditschedulers().remove(organizationadcreditscheduler);
	 * organizationadcreditscheduler.setLookup(null);
	 * 
	 * return organizationadcreditscheduler; }
	 */

	public List<OrganizationCategory> getOrganizationcategories() {
		return this.organizationcategories;
	}

	public void setOrganizationcategories(List<OrganizationCategory> organizationcategories) {
		this.organizationcategories = organizationcategories;
	}

	public OrganizationCategory addOrganizationcategory(OrganizationCategory organizationcategory) {
		getOrganizationcategories().add(organizationcategory);
		organizationcategory.setLookup(this);

		return organizationcategory;
	}

	public OrganizationCategory removeOrganizationcategory(OrganizationCategory organizationcategory) {
		getOrganizationcategories().remove(organizationcategory);
		organizationcategory.setLookup(null);

		return organizationcategory;
	}

	/*
	 * public List<Organizationplansubscription> getOrganizationplansubscriptions1()
	 * { return this.organizationplansubscriptions1; }
	 * 
	 * public void
	 * setOrganizationplansubscriptions1(List<Organizationplansubscription>
	 * organizationplansubscriptions1) { this.organizationplansubscriptions1 =
	 * organizationplansubscriptions1; }
	 */

	/*
	 * public Organizationplansubscription
	 * addOrganizationplansubscriptions1(Organizationplansubscription
	 * organizationplansubscriptions1) {
	 * getOrganizationplansubscriptions1().add(organizationplansubscriptions1);
	 * organizationplansubscriptions1.setLookup1(this);
	 * 
	 * return organizationplansubscriptions1; }
	 * 
	 * public Organizationplansubscription
	 * removeOrganizationplansubscriptions1(Organizationplansubscription
	 * organizationplansubscriptions1) {
	 * getOrganizationplansubscriptions1().remove(organizationplansubscriptions1);
	 * organizationplansubscriptions1.setLookup1(null);
	 * 
	 * return organizationplansubscriptions1; }
	 */

	/*
	 * public List<Organizationplansubscription> getOrganizationplansubscriptions2()
	 * { return this.organizationplansubscriptions2; }
	 * 
	 * public void
	 * setOrganizationplansubscriptions2(List<Organizationplansubscription>
	 * organizationplansubscriptions2) { this.organizationplansubscriptions2 =
	 * organizationplansubscriptions2; }
	 */

	/*
	 * public Organizationplansubscription
	 * addOrganizationplansubscriptions2(Organizationplansubscription
	 * organizationplansubscriptions2) {
	 * getOrganizationplansubscriptions2().add(organizationplansubscriptions2);
	 * organizationplansubscriptions2.setLookup2(this);
	 * 
	 * return organizationplansubscriptions2; }
	 * 
	 * public Organizationplansubscription
	 * removeOrganizationplansubscriptions2(Organizationplansubscription
	 * organizationplansubscriptions2) {
	 * getOrganizationplansubscriptions2().remove(organizationplansubscriptions2);
	 * organizationplansubscriptions2.setLookup2(null);
	 * 
	 * return organizationplansubscriptions2; }
	 * 
	 * public List<Planfeature> getPlanfeatures() { return this.planfeatures; }
	 * 
	 * public void setPlanfeatures(List<Planfeature> planfeatures) {
	 * this.planfeatures = planfeatures; }
	 * 
	 * public Planfeature addPlanfeature(Planfeature planfeature) {
	 * getPlanfeatures().add(planfeature); planfeature.setLookup(this);
	 * 
	 * return planfeature; }
	 * 
	 * public Planfeature removePlanfeature(Planfeature planfeature) {
	 * getPlanfeatures().remove(planfeature); planfeature.setLookup(null);
	 * 
	 * return planfeature; }
	 * 
	 * public List<Planprice> getPlanprices1() { return this.planprices1; }
	 * 
	 * public void setPlanprices1(List<Planprice> planprices1) { this.planprices1 =
	 * planprices1; }
	 * 
	 * public Planprice addPlanprices1(Planprice planprices1) {
	 * getPlanprices1().add(planprices1); planprices1.setLookup1(this);
	 * 
	 * return planprices1; }
	 * 
	 * public Planprice removePlanprices1(Planprice planprices1) {
	 * getPlanprices1().remove(planprices1); planprices1.setLookup1(null);
	 * 
	 * return planprices1; }
	 * 
	 * public List<Planprice> getPlanprices2() { return this.planprices2; }
	 * 
	 * public void setPlanprices2(List<Planprice> planprices2) { this.planprices2 =
	 * planprices2; }
	 * 
	 * public Planprice addPlanprices2(Planprice planprices2) {
	 * getPlanprices2().add(planprices2); planprices2.setLookup2(this);
	 * 
	 * return planprices2; }
	 * 
	 * public Planprice removePlanprices2(Planprice planprices2) {
	 * getPlanprices2().remove(planprices2); planprices2.setLookup2(null);
	 * 
	 * return planprices2; }
	 * 
	 * public List<Planprice> getPlanprices3() { return this.planprices3; }
	 * 
	 * public void setPlanprices3(List<Planprice> planprices3) { this.planprices3 =
	 * planprices3; }
	 * 
	 * public Planprice addPlanprices3(Planprice planprices3) {
	 * getPlanprices3().add(planprices3); planprices3.setLookup3(this);
	 * 
	 * return planprices3; }
	 * 
	 * public Planprice removePlanprices3(Planprice planprices3) {
	 * getPlanprices3().remove(planprices3); planprices3.setLookup3(null);
	 * 
	 * return planprices3; }
	 * 
	 * public List<Planprice> getPlanprices4() { return this.planprices4; }
	 * 
	 * public void setPlanprices4(List<Planprice> planprices4) { this.planprices4 =
	 * planprices4; }
	 * 
	 * public Planprice addPlanprices4(Planprice planprices4) {
	 * getPlanprices4().add(planprices4); planprices4.setLookup4(this);
	 * 
	 * return planprices4; }
	 * 
	 * public Planprice removePlanprices4(Planprice planprices4) {
	 * getPlanprices4().remove(planprices4); planprices4.setLookup4(null);
	 * 
	 * return planprices4; }
	 */

	public List<Userrole> getUserroles() {
		return this.userroles;
	}

	public void setUserroles(List<Userrole> userroles) {
		this.userroles = userroles;
	}

	public Userrole addUserrole(Userrole userrole) {
		getUserroles().add(userrole);
		userrole.setLookup(this);

		return userrole;
	}

	public Userrole removeUserrole(Userrole userrole) {
		getUserroles().remove(userrole);
		userrole.setLookup(null);

		return userrole;
	}

}
