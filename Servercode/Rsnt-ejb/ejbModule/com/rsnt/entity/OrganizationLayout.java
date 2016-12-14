package com.rsnt.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.rsnt.common.entity.BaseEntity;

@Entity(name="OrganizationLayout")
@Table(name="ORGANIZATIONLAYOUT")
@NamedQueries({
	@NamedQuery(name=OrganizationLayout.FETCH_ORGANIZATION_LAYOUT_OBJ,query="select l from OrganizationLayout l where l.organizationLayoutId = (?1) and l.organizationId = (?2)"),
	@NamedQuery(name=OrganizationLayout.FETCH_ORGANIZATION_LAYOUT_OBJ_OPTIONS,query="select l from OrganizationLayout l " +
			"left join fetch l.organizationLayoutOptionList organizationLayoutOptionList where l.organizationLayoutId = (?1) and l.organizationId = (?2)"),
	@NamedQuery(name=OrganizationLayout.FETCH_ORGANIZATION_LAYOUT_FROM_LAYOUTNAME, query="Select o from OrganizationLayout o where lower(o.layoutIdentificationName) = ?1 and o.organizationId = ?2"),
	@NamedQuery(name=OrganizationLayout.FETCH_ORGANIZATION_LAYOUT_FROM_LAYOUTNAME_AND_ID, query="Select o from OrganizationLayout o where lower(o.layoutIdentificationName) = ?1 and o.organizationId = ?2 and o.organizationLayoutId!=?3")		
})
public class OrganizationLayout extends BaseEntity {
	
	public static final String FETCH_ORGANIZATION_LAYOUT_OBJ = "fetchOrganizationLayoutObj";
	
	public static final String FETCH_ORGANIZATION_LAYOUT_OBJ_OPTIONS = "fetchOrganizationLayoutObjOptions";

	public static final String FETCH_ORGANIZATION_LAYOUT_FROM_LAYOUTNAME= "fetchOrganizationFromLayoutName";
	public static final String FETCH_ORGANIZATION_LAYOUT_FROM_LAYOUTNAME_AND_ID= "fetchOrganizationFromLayoutNameAndId";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="OrganizationLayoutId")
	private Long organizationLayoutId;
	
	@Column(name="OrganizationId")
	private Long organizationId;
	
	@Column(name="LayoutIdentificationName")
	private String layoutIdentificationName;
	
	@Column(name="LayoutCapacity")
	private Long layoutCapacity;
	
	/*@Type(type="yes_no")
	@Column(name="Default")
	private Char isDefault;*/
	
	/*@Column(columnDefinition = "BOOLEAN",name="isDefault")
	private boolean defaultFlag;
	*/
	/*@Column(columnDefinition = "bit",name="activeFlag")
	private boolean activeFlag;
	*/
	
	@Column(columnDefinition = "TINYINT",name="activeFlag", nullable=false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean activeFlag;
	
	@Column(columnDefinition = "TINYINT" , name="isDefault" ,nullable=false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean defaultFlag;
	
	@OneToMany(mappedBy="organizationLayout",fetch = FetchType.LAZY,cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	private List<OrganizationLayoutMarkers> organizationLayoutMarkerList = new ArrayList<OrganizationLayoutMarkers>();

	@OneToMany(mappedBy="organizationLayout",fetch = FetchType.LAZY)
	@org.hibernate.annotations.Cascade({ org.hibernate.annotations.CascadeType.ALL,
             org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	private List<OrganizationLayoutOption> organizationLayoutOptionList = new ArrayList<OrganizationLayoutOption>();


	public Long getOrganizationLayoutId() {
		return organizationLayoutId;
	}


	public void setOrganizationLayoutId(Long organizationLayoutId) {
		this.organizationLayoutId = organizationLayoutId;
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


	public Long getLayoutCapacity() {
		return layoutCapacity;
	}


	public void setLayoutCapacity(Long layoutCapacity) {
		this.layoutCapacity = layoutCapacity;
	}


	public List<OrganizationLayoutMarkers> getOrganizationLayoutMarkerList() {
		return organizationLayoutMarkerList;
	}


	public void setOrganizationLayoutMarkerList(
			List<OrganizationLayoutMarkers> organizationLayoutMarkerList) {
		this.organizationLayoutMarkerList = organizationLayoutMarkerList;
	}


	public List<OrganizationLayoutOption> getOrganizationLayoutOptionList() {
		return organizationLayoutOptionList;
	}


	public void setOrganizationLayoutOptionList(
			List<OrganizationLayoutOption> organizationLayoutOptionList) {
		this.organizationLayoutOptionList = organizationLayoutOptionList;
	}


	public boolean isDefaultFlag() {
		return defaultFlag;
	}


	public void setDefaultFlag(boolean defaultFlag) {
		this.defaultFlag = defaultFlag;
	}


	public boolean isActiveFlag() {
		return activeFlag;
	}


	public void setActiveFlag(boolean activeFlag) {
		this.activeFlag = activeFlag;
	}


	
	
	
	

}
