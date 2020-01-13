package com.kyobeeUserService.dto;

public class LanguageMasterDTO {

	 private Integer LangId;
	 private String LangName;
	 private String LangIsoCode;
	 private String keyName;
	 private String Value;
	 
	 public LanguageMasterDTO(Integer LangId,String LangName,String LangIsoCode,String keyName,String Value) {
		 this.LangId=LangId;
		 this.LangName=LangName;
		 this.LangIsoCode=LangIsoCode;
		 this.keyName=keyName;
		 this.Value=Value;
		 
	 }
	 
	public Integer getLangId() {
		return LangId;
	}
	public void setLangId(Integer langId) {
		LangId = langId;
	}
	public String getLangName() {
		return LangName;
	}
	public void setLangName(String langName) {
		LangName = langName;
	}
	public String getLangIsoCode() {
		return LangIsoCode;
	}
	public void setLangIsoCode(String langIsoCode) {
		LangIsoCode = langIsoCode;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	 
}
