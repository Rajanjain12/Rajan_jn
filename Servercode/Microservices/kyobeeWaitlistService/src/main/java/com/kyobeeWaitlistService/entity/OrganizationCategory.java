package com.kyobeeWaitlistService.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
@Table(name="ORGANIZATIONCATEGORY")
public class OrganizationCategory implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="OrganizationCategoryID")
	private Integer organizationCategoryID;

	//bi-directional many-to-one association to Lookuptype
	@ManyToOne
	@JoinColumn(name="CategoryTypeID")
	private LookupType lookuptype;

	//bi-directional many-to-one association to Lookup
	@ManyToOne
	@JoinColumn(name="CategoryValueID")
	private Lookup lookup;

	//bi-directional many-to-one association to Organization
	@ManyToOne
	@JoinColumn(name="OrganizationID")
	private Organization organization;
	
	public OrganizationCategory() {
		//constructor 
	}

	public Integer getOrganizationCategoryID() {
		return this.organizationCategoryID;
	}

	public void setOrganizationCategoryID(Integer organizationCategoryID) {
		this.organizationCategoryID = organizationCategoryID;
	}

	public LookupType getLookuptype() {
		return this.lookuptype;
	}

	public void setLookuptype(LookupType lookuptype) {
		this.lookuptype = lookuptype;
	}

	public Lookup getLookup() {
		return this.lookup;
	}

	public void setLookup(Lookup lookup) {
		this.lookup = lookup;
	}

	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}
