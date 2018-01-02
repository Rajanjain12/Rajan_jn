package com.kyobee.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
@XmlRootElement
public class OrganizationTemplateDTO {
private Long smsTemplateID;
private String templateText;
private Long LanguageID;
private Integer level;

@XmlAttribute
public Long getSmsTemplateID() {
	return smsTemplateID;
}
public void setSmsTemplateID(Long smsTemplateID) {
	this.smsTemplateID = smsTemplateID;
}

@XmlAttribute
public String getTemplateText() {
	return templateText;
}
public void setTemplateText(String templateText) {
	this.templateText = templateText;
}

@XmlAttribute
public Long getLanguageID() {
	return LanguageID;
}
public void setLanguageID(Long languageID) {
	LanguageID = languageID;
}

@XmlAttribute
public Integer getLevel() {
	return level;
}
public void setLevel(Integer level) {
	this.level = level;
}
}
