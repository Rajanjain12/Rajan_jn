package com.kyobee.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
@XmlRootElement
public class LanguageMasterDTO {
 private Long langId;
 private String langName;
 private String langIsoCode;
 
 @XmlAttribute
public Long getLangId() {
	return langId;
}
public void setLangId(Long langId) {
	this.langId = langId;
}

@XmlAttribute
public String getLangName() {
	return langName;
}
public void setLangName(String langName) {
	this.langName = langName;
}

@XmlAttribute
public String getLangIsoCode() {
	return langIsoCode;
}
public void setLangIsoCode(String langIsoCode) {
	this.langIsoCode = langIsoCode;
}
}
