package com.rsnt.util.transferobject;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.jboss.seam.annotations.Name;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Name("planDataTO")
public class PlanDataTO {

	@JsonProperty
	private String planId;
	
	@JsonProperty
	private String planDescription;

	@JsonProperty
	private String planType;
	
	@JsonProperty
	private String active;
	
	@JsonProperty
	private Long access;

	
	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getPlanDescription() {
		return planDescription;
	}

	public void setPlanDescription(String planDescription) {
		this.planDescription = planDescription;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public Long getAccess() {
		return access;
	}

	public void setAccess(Long access) {
		this.access = access;
	}
}
