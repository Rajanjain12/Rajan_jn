package com.kyobee.dto;

import java.util.ArrayList;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
@XmlRootElement
public class LanguageMasterDTO {
 private Long langId;
 private String langName;
/* private String langDisplayName;*/
 private String langIsoCode;
 
 // object of language preference
 private Map<String, String> languageMap;
 
 
 @XmlAttribute
public Map<String, String> getLanguageMap() {
	return languageMap;
}
public void setLanguageMap(Map<String, String> languageMap) {
	this.languageMap = languageMap;
}
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

/*@XmlAttribute
public String getLangDisplayName() {
	return langDisplayName;
}
public void setLangDisplayName(String langDisplayName) {
	this.langDisplayName = langDisplayName;
}*/
}
