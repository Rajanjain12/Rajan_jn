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


}
