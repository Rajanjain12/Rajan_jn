package com.kyobee.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
@XmlRootElement
public class GuestPreferencesDTO {
	private Long guestPrefId;
	private Long prefValueId;
	private String prefValue;
	
	@XmlAttribute
	public Long getGuestPrefId() {
		return guestPrefId;
	}
	public void setGuestPrefId(Long guestPrefId) {
		this.guestPrefId = guestPrefId;
	}
	@XmlAttribute
	public Long getPrefValueId() {
		return prefValueId;
	}
	public void setPrefValueId(Long prefValueId) {
		this.prefValueId = prefValueId;
	}
	@XmlAttribute
	public String getPrefValue() {
		return prefValue;
	}
	public void setPrefValue(String prefValue) {
		this.prefValue = prefValue;
	}
	
	
}
