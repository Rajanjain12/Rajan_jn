package com.kyobee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LANGUAGEKEYMAPPING")
public class LanguageKeyMapping {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name ="LanguageKeyMappingId")
	private Long languageKeyMappingId;
	
	@Column(name ="keyName")
	private String keyName;
	
	@Column(name ="Value")
	private String value;
	
	@Column(name ="LangIsoCode")
	private String langIsoCode;
	
	@Column(name ="ScreenName")
	private String screenName;

	public Long getLanguageKeyMappingId() {
		return languageKeyMappingId;
	}

	public void setLanguageKeyMappingId(Long languageKeyMappingId) {
		this.languageKeyMappingId = languageKeyMappingId;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLangIsoCode() {
		return langIsoCode;
	}

	public void setLangIsoCode(String langIsoCode) {
		this.langIsoCode = langIsoCode;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	
}