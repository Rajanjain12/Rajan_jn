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
@Table(name = "LOOKUP")
public class Lookup implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "LookupID")
	private Integer lookupID;

	@Column(name = "Code")
	private String code;

	@Column(name = "Description")
	private String description;

	@Column(name = "Filter")
	private Integer filter;

	@Column(name = "Name")
	private String name;

	@Column(name = "Position")
	private Integer position;

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

	// bi-directional many-to-one association to Lookuptype
	@ManyToOne
	@JoinColumn(name = "LookupTypeID")
	private LookupType lookuptype;

	// bi-directional many-to-one association to Organizationcategory
	@OneToMany(mappedBy = "lookup")
	private List<OrganizationCategory> organizationcategories;

	public Lookup() {
	}


	public Integer getLookupID() {
		return lookupID;
	}


	public void setLookupID(Integer lookupID) {
		this.lookupID = lookupID;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Integer getFilter() {
		return filter;
	}


	public void setFilter(Integer filter) {
		this.filter = filter;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Integer getPosition() {
		return position;
	}


	public void setPosition(Integer position) {
		this.position = position;
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


	public LookupType getLookuptype() {
		return lookuptype;
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
