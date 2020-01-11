package com.kyobeeUserService.dto;

public class LanguageMasterDTO {

	 private Long langId;
	 private String langName;
	 private String langIsoCode;

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
	 public String getLangIsoCode() {
		return langIsoCode;
	 }
	 public void setLangIsoCode(String langIsoCode) {
		this.langIsoCode = langIsoCode;
	 }
	 
}
