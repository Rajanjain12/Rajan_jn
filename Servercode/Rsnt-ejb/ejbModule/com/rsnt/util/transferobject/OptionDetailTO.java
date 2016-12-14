package com.rsnt.util.transferobject;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.jboss.seam.annotations.Name;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Name("optionDetailTO")
public class OptionDetailTO {

	@JsonProperty
	private String orgOptionId;
	
	@JsonProperty
	private String description;

	public String getOrgOptionId() {
		return orgOptionId;
	}

	public void setOrgOptionId(String orgOptionId) {
		this.orgOptionId = orgOptionId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
