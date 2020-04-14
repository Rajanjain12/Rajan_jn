package com.kyobeeUserService.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="ORGANIZATIONLANG")
public class OrganizationLang implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="OrganizationLangID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer organizationLangID;

	@Column(name="Active")
	private Byte active;

	@Temporal(TemporalType.DATE)
	@Column(name="CreatedAt")
	private Date createdAt;

	@Column(name="CreatedBy")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="ModifiedAt")
	private Date modifiedAt;

	@Column(name="ModifiedBy")
	private String modifiedBy;

	//bi-directional many-to-one association to Organization
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="OrganizationID")
	private Organization organization;

	//bi-directional many-to-one association to Langmaster
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="LanguageID")
	private LangMaster langmaster;

	public OrganizationLang() {
		//constructor 
	}

	public Integer getOrganizationLangID() {
		return this.organizationLangID;
	}

	public void setOrganizationLangID(Integer organizationLangID) {
		this.organizationLangID = organizationLangID;
	}

	public Byte getActive() {
		return this.active;
	}

	public void setActive(Byte active) {
		this.active = active;
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

	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public LangMaster getLangmaster() {
		return this.langmaster;
	}

	public void setLangmaster(LangMaster langmaster) {
		this.langmaster = langmaster;
	}

}
