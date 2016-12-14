package com.rsnt.util.transferobject;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.jboss.seam.annotations.Name;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Name("layoutDataTO")
public class LayoutDataTO implements Serializable{
	
	@JsonProperty
	private String orgLayoutId;
	
	@JsonProperty
	private String description;
	
	@JsonProperty
	private String isDefault;
	

	@JsonProperty
	private String activeFlag;
	
	
	@JsonProperty
	private String layoutCapacity;
	
	@JsonProperty
	private String createdBy;
	
	@JsonProperty
	private String createdAt;
	
	@JsonProperty
	private String updatedBy;
	
	@JsonProperty
	private String updatedAt;
	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getOrgLayoutId() {
		return orgLayoutId;
	}

	public void setOrgLayoutId(String orgLayoutId) {
		this.orgLayoutId = orgLayoutId;
	}

	public String getLayoutCapacity() {
		return layoutCapacity;
	}

	public void setLayoutCapacity(String layoutCapacity) {
		this.layoutCapacity = layoutCapacity;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}
	
}
