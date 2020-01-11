package com.kyobeeUserService.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="ORGANIZATIONTEMPLATE")
public class OrganizationTemplate implements Serializable{

	@Id
	@Column(name="SmsTemplateID")
	private Integer smsTemplateID;

	@Column(name="Active")
	private Byte active;

	@Temporal(TemporalType.DATE)
	@Column(name="CreatedAt")
	private Date createdAt;

	@Column(name="CreatedBy")
	private String createdBy;

	@Column(name="LanguageID")
	private Integer languageID;

	@Column(name="Level")
	private Integer level;

	@Temporal(TemporalType.DATE)
	@Column(name="ModifiedAt")
	private Date modifiedAt;

	@Column(name="ModifiedBy")
	private String modifiedBy;

	@Column(name="TemplateText")
	private String templateText;

	//bi-directional many-to-one association to Organization
	@ManyToOne
	@JoinColumn(name="OrgID")
	private Organization organization;

	public OrganizationTemplate() {
	}

	public Integer getSmsTemplateID() {
		return this.smsTemplateID;
	}

	public void setSmsTemplateID(Integer smsTemplateID) {
		this.smsTemplateID = smsTemplateID;
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

	public Integer getLanguageID() {
		return this.languageID;
	}

	public void setLanguageID(Integer languageID) {
		this.languageID = languageID;
	}

	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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

	public String getTemplateText() {
		return this.templateText;
	}

	public void setTemplateText(String templateText) {
		this.templateText = templateText;
	}

	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}
