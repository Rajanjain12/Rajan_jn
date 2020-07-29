package com.kyobeeAccountService.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "LANGMASTER")
public class LangMaster implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "LangID")
	private Integer langID;

	@Column(name = "LangName")
	private String langName;

	@Column(name = "LanguageDisplayName")
	private String languageDisplayName;

	@Column(name = "LangIsoCode")
	private String langIsoCode;

	@Column(name = "Active")
	private Byte active;

	@Column(name = "CreatedBy")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CreatedAt")
	private Date createdAt;

	@Temporal(TemporalType.DATE)
	@Column(name = "ModifiedAt")
	private Date modifiedAt;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	// bi-directional many-to-one association to Guest
	@OneToMany(mappedBy = "langmaster")
	private List<Guest> guests;

	// bi-directional many-to-one association to Organizationlang
	@OneToMany(mappedBy = "langmaster")
	private List<OrganizationLang> organizationlangs;

	public void Langmaster() {
		// constructor
	}



	public Integer getLangID() {
		return langID;
	}



	public void setLangID(Integer langID) {
		this.langID = langID;
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



	public Byte getActive() {
		return active;
	}



	public void setActive(Byte active) {
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



	public void setOrganizationlangs(List<OrganizationLang> organizationlangs) {
		this.organizationlangs = organizationlangs;
	}



	public List<Guest> getGuests() {
		return this.guests;
	}

	public void setGuests(List<Guest> guests) {
		this.guests = guests;
	}

	public Guest addGuest(Guest guest) {
		getGuests().add(guest);
		guest.setLangmaster(this);

		return guest;
	}

	public Guest removeGuest(Guest guest) {
		getGuests().remove(guest);
		guest.setLangmaster(null);

		return guest;
	}

	public List<OrganizationLang> getOrganizationlangs() {
		return this.organizationlangs;
	}

	public void setOrganizationLangs(List<OrganizationLang> organizationlangs) {
		this.organizationlangs = organizationlangs;
	}

	public OrganizationLang addOrganizationlang(OrganizationLang organizationlang) {
		getOrganizationlangs().add(organizationlang);
		organizationlang.setLangmaster(this);

		return organizationlang;
	}

	public OrganizationLang removeOrganizationlang(OrganizationLang organizationlang) {
		getOrganizationlangs().remove(organizationlang);
		organizationlang.setLangmaster(null);

		return organizationlang;
	}
}
