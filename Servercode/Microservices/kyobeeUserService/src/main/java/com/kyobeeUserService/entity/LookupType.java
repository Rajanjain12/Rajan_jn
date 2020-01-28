package com.kyobeeUserService.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="LOOKUPTYPE")
public class LookupType implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="LookupTypeID")
	private Integer lookupTypeID;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CreatedAt")
	private Date createdAt;

	@Column(name="CreatedBy")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ModifiedAt")
	private Date modifiedAt;

	@Column(name="ModifiedBy")
	private String modifiedBy;

	@Column(name="Name")
	private String name;

	//bi-directional many-to-one association to Lookup
	@OneToMany(mappedBy="lookuptype")
	private List<Lookup> lookups;

	//bi-directional many-to-one association to Organizationcategory
	@OneToMany(mappedBy="lookuptype")
	private List<OrganizationCategory> organizationcategories;

	public LookupType() {
		//constructor 
	}

	public Integer getLookupTypeID() {
		return this.lookupTypeID;
	}

	public void setLookupTypeID(Integer lookupTypeID) {
		this.lookupTypeID = lookupTypeID;
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

	public List<Lookup> getLookups() {
		return this.lookups;
	}

	public void setLookups(List<Lookup> lookups) {
		this.lookups = lookups;
	}

	public Lookup addLookup(Lookup lookup) {
		getLookups().add(lookup);
		lookup.setLookuptype(this);

		return lookup;
	}

	public Lookup removeLookup(Lookup lookup) {
		getLookups().remove(lookup);
		lookup.setLookuptype(null);

		return lookup;
	}

	public List<OrganizationCategory> getOrganizationcategories() {
		return this.organizationcategories;
	}

	public void setOrganizationcategories(List<OrganizationCategory> organizationcategories) {
		this.organizationcategories = organizationcategories;
	}

	public OrganizationCategory addOrganizationcategory(OrganizationCategory organizationcategory) {
		getOrganizationcategories().add(organizationcategory);
		organizationcategory.setLookuptype(this);

		return organizationcategory;
	}

	public OrganizationCategory removeOrganizationcategory(OrganizationCategory organizationcategory) {
		getOrganizationcategories().remove(organizationcategory);
		organizationcategory.setLookuptype(null);

		return organizationcategory;
	}
}
