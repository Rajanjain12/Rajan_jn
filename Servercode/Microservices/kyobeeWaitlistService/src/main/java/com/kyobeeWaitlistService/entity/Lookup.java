package com.kyobeeWaitlistService.entity;

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

	

	//bi-directional many-to-one association to Organizationcategory
	@OneToMany(mappedBy="lookup")
	private List<OrganizationCategory> organizationcategories;

	
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
