package com.kyobee.dto;

/*
created by arjun patel for seating preference 
06/12/2019  
*/
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
@XmlRootElement
public class GuestPreferencesDTOV2 {
	private Long guestPrefId;
	private Long prefValueId;
	private String prefValue;
	private String prefKey; 	
	
	@XmlAttribute
	public String getPrefKey() {
		return prefKey;
	}
	public void setPrefKey(String prefKey) {
		this.prefKey = prefKey;
	}
	private boolean selected;
	
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
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}	
	
}
