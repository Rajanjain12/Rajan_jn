package com.kyobeeWaitlistService.dto;

public class LanguageMasterDTO {

	 private Integer langID;
	 private String langName;
	 private String langIsoCode;
	 private String keyName;
	 private String value;
	 
	 public LanguageMasterDTO() {
		 	
	 }
	 public LanguageMasterDTO(Integer langId,String langName,String langIsoCode,String keyName,String value) {
		 this.langID=langId;
		 this.langName=langName;
		 this.langIsoCode=langIsoCode;
		 this.keyName=keyName;
		 this.value=value;
		 
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
	public String getLangIsoCode() {
		return langIsoCode;
	}
	public void setLangIsoCode(String langIsoCode) {
		this.langIsoCode = langIsoCode;
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
	 
	 
}
