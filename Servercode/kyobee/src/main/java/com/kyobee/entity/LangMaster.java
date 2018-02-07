package com.kyobee.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="LANGMASTER")
public class LangMaster implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="LangID")
	private Long langId;
	
	@Column(name="LangName")
	private String langName;
	
	@Column(name="LanguageDisplayName")
	private String languageDisplayName;
	
	@Column(name="LangIsoCode")
	private String langIsoCode;
	
	@Column(columnDefinition = "TINYINT",name="Active", nullable=false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean active;
	
	@Column(name="CreatedBy")
	private String createdBy;
	
	@Column(name="CreatedAt")
	private Date createdAt;
	
	@Column(name="ModifiedBy")
	private String modifiedBy;
	
	@Column(name="ModifiedAt")
	private Date modifiedAt;

	public Long getLangId() {
		return langId;
	}

	public void setLangId(Long langId) {
		this.langId = langId;
	}

	public String getLangName() {
		return langName;
	}

	public void setLangName(String langName) {
		this.langName = langName;
	}

	public String getLanguageDisplayName() {
		return languageDisplayName;
	}

	public void setLanguageDisplayName(String languageDisplayName) {
		this.languageDisplayName = languageDisplayName;
	}

	public String getLangIsoCode() {
		return langIsoCode;
	}

	public void setLangIsoCode(String langIsoCode) {
		this.langIsoCode = langIsoCode;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
}
