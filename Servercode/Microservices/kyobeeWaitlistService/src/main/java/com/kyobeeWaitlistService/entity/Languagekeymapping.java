package com.kyobeeWaitlistService.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="LANGUAGEKEYMAPPING")
public class Languagekeymapping implements Serializable{

	
		private static final long serialVersionUID = 1L;

		@Id
		@Column(name="LanguageKeyMappingId")
		private int languageKeyMappingId;

		@Temporal(TemporalType.DATE)
		@Column(name="CreatedAt")
		private Date createdAt;

		@Column(name="CreatedBy")
		private String createdBy;

		private String keyName;

		@Column(name="LangIsoCode")
		private String langIsoCode;

		@Temporal(TemporalType.DATE)
		@Column(name="ModifiedAt")
		private Date modifiedAt;

		@Column(name="ModifiedBy")
		private String modifiedBy;

		@Column(name="ScreenName")
		private String screenName;

		@Column(name="UpdateFlag")
		private int updateFlag;

		@Column(name="Value")
		private String value;

		public Languagekeymapping() {
		}

		public int getLanguageKeyMappingId() {
			return this.languageKeyMappingId;
		}

		public void setLanguageKeyMappingId(int languageKeyMappingId) {
			this.languageKeyMappingId = languageKeyMappingId;
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

		public String getKeyName() {
			return this.keyName;
		}

		public void setKeyName(String keyName) {
			this.keyName = keyName;
		}

		public String getLangIsoCode() {
			return this.langIsoCode;
		}

		public void setLangIsoCode(String langIsoCode) {
			this.langIsoCode = langIsoCode;
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

		public String getScreenName() {
			return this.screenName;
		}

		public void setScreenName(String screenName) {
			this.screenName = screenName;
		}

		public int getUpdateFlag() {
			return this.updateFlag;
		}

		public void setUpdateFlag(int updateFlag) {
			this.updateFlag = updateFlag;
		}

		public String getValue() {
			return this.value;
		}

		public void setValue(String value) {
			this.value = value;
		}
}
