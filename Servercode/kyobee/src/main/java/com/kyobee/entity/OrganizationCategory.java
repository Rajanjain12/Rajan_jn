package com.kyobee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="ORGANIZATIONCATEGORY")
public class OrganizationCategory {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="OrganizationCategoryId")
	private Long organizationCategoryId;
	
	
	@JoinColumn(name="OrganizationId")
	@ManyToOne(fetch = FetchType.LAZY)
	private Organization organization;
	
	
	private Long categoryTypeId;
	private Long categoryValueId;
	
	
	public Long getOrganizationCategoryId() {
		return organizationCategoryId;
	}
	public void setOrganizationCategoryId(Long organizationCategoryId) {
		this.organizationCategoryId = organizationCategoryId;
	}
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	public Long getCategoryTypeId() {
		return categoryTypeId;
	}
	public void setCategoryTypeId(Long categoryTypeId) {
		this.categoryTypeId = categoryTypeId;
	}
	public Long getCategoryValueId() {
		return categoryValueId;
	}
	public void setCategoryValueId(Long categoryValueId) {
		this.categoryValueId = categoryValueId;
	}
	
	
}
