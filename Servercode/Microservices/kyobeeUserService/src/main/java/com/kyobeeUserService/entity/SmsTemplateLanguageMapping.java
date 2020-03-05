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
@Table(name="SMSTEMPLATELANGUAGEMAPPING")
public class SmsTemplateLanguageMapping implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Integer Id;
	
	@Column(name="IsoCode")
	private String isoCode;
	
	@Column(name="TemplateText")
	private String templateText;
	
	@Column(name="Level")
	private Integer level;

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
	
	//bi-directional many-to-one association to Langmaster
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="LangId")
	private LangMaster langmaster;

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}
	
	public String getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String getTemplateText() {
		return templateText;
	}

	public void setTemplateText(String templateText) {
		this.templateText = templateText;
	}
	
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Byte getActive() {
		return active;
	}

	public void setActive(Byte active) {
		this.active = active;
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

	public LangMaster getLangmaster() {
		return langmaster;
	}

	public void setLangmaster(LangMaster langmaster) {
		this.langmaster = langmaster;
	}
	
}
