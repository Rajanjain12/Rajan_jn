package com.kyobeeUserService.dto;

public class SmsTemplateDTO {
 
	Integer smsTemplateID;
	String templateText;
	Integer level;
	Integer languageID;
	String levelName;
	
	public Integer getSmsTemplateID() {
		return smsTemplateID;
	}
	public void setSmsTemplateID(Integer smsTemplateID) {
		this.smsTemplateID = smsTemplateID;
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
	public Integer getLanguageID() {
		return languageID;
	}
	public void setLanguageID(Integer languageID) {
		this.languageID = languageID;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	
	
}
