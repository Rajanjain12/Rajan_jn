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
@Entity(name="OrganizationLayoutOption")
@Table(name="ORGANIZATIONLAYOUTOPTION")
public class OrganizationLayoutOption extends BaseEntity{
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="OrganizationLayoutOptionId")
	private Long organizationLayoutOptionId;
	
	@Column(name="OrganizationId")
	private Long organizationId;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="OrganizationLayoutId")
	private OrganizationLayout organizationLayout;
	
	@Column(name="LayoutIdentificationName")
	private String layoutIdentificationName;
	
	@Column(name="OrganizationOptionId")
	private Long organizationOptionId;

	public Long getOrganizationLayoutOptionId() {
		return organizationLayoutOptionId;
	}

	public void setOrganizationLayoutOptionId(Long organizationLayoutOptionId) {
		this.organizationLayoutOptionId = organizationLayoutOptionId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}


	public String getLayoutIdentificationName() {
		return layoutIdentificationName;
	}

	public void setLayoutIdentificationName(String layoutIdentificationName) {
		this.layoutIdentificationName = layoutIdentificationName;
	}

	public Long getOrganizationOptionId() {
		return organizationOptionId;
	}

	public void setOrganizationOptionId(Long organizationOptionId) {
		this.organizationOptionId = organizationOptionId;
	}

	public OrganizationLayout getOrganizationLayout() {
		return organizationLayout;
	}

	public void setOrganizationLayout(OrganizationLayout organizationLayout) {
		this.organizationLayout = organizationLayout;
	}



}
