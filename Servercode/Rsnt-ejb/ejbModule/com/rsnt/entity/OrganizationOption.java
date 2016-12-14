package com.rsnt.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.rsnt.common.entity.BaseEntity;

@Entity(name="OrganizationOption")
@Table(name="ORGANIZATIONOPTION")
@NamedQueries({@NamedQuery(name=OrganizationOption.GET_ORGANIZATION_OPTIONS,query="Select opt from OrganizationOption opt where opt.organization.organizationId=?1 ")})
public class OrganizationOption extends BaseEntity {
	
	public static final String GET_ORGANIZATION_OPTIONS = "getOrganizationOptions";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="OrganizationOptionId")
	private Long organizationOptionId;
	
	@JoinColumn(name="OrganizationId")
	@ManyToOne(fetch = FetchType.LAZY,cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	private Organization organization;
	
	@Column(name="OptionDescription")
	private String optionDescription;

	public Long getOrganizationOptionId() {
		return organizationOptionId;
	}

	public void setOrganizationOptionId(Long organizationOptionId) {
		this.organizationOptionId = organizationOptionId;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getOptionDescription() {
		return optionDescription;
	}

	public void setOptionDescription(String optionDescription) {
		this.optionDescription = optionDescription;
	}
	
	

}
