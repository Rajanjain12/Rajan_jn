package com.kyobeeWaitlistService.dto;

public class OrganizationTemplateDTO {

	private Integer smsTemplateID;
	private String templateText;
	private Integer languageID;
	private Integer level;
	
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
	public Integer getLanguageID() {
		return languageID;
	}
	public void setLanguageID(Integer languageID) {
		this.languageID = languageID;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	
}
