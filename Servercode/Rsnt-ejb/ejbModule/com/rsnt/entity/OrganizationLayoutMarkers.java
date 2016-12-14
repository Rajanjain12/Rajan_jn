package com.rsnt.entity;

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

import org.hibernate.annotations.Type;

import com.rsnt.common.entity.BaseEntity;

@Entity(name="OrganizationLayoutMarkers")
@Table(name="ORGANIZATIONLAYOUTMARKERS")
@NamedQueries({
	@NamedQuery(name=OrganizationLayoutMarkers.GET_MARKER_DATA_FOR_LAYOUT,query="Select l from OrganizationLayoutMarkers l where l.organizationLayout.organizationLayoutId = (?1) and l.organizationId= (?2) "),
	@NamedQuery(name=OrganizationLayoutMarkers.GET_ACTIVE_MARKER_DATA_FOR_LAYOUT,query="Select l from OrganizationLayoutMarkers l where l.organizationLayout.organizationLayoutId = (?1) and l.organizationId= (?2) and l.active is true "),
	@NamedQuery(name=OrganizationLayoutMarkers.GET_MARKER_DATA_FOR_DEFAULT_ORG_LAYOUT,query="Select l from OrganizationLayoutMarkers l inner join fetch l.organizationLayout organizationLayout where " +
			"organizationLayout.organizationId = ?1 and organizationLayout.defaultFlag is true and organizationLayout.activeFlag is true"),
	@NamedQuery(name= OrganizationLayoutMarkers.GET_LAYOUT_MARKER_FROM_MARKERNAME, query= "Select o from OrganizationLayoutMarkers o where lower(o.layoutMarkerCode) = ?1 and o.organizationId = ?2 and o.organizationLayout.organizationLayoutId = ?3"),
	@NamedQuery(name= OrganizationLayoutMarkers.GET_LAYOUT_MARKER_FROM_MARKERNAME_WITHID, query= "Select o from OrganizationLayoutMarkers o where lower(o.layoutMarkerCode) = ?1 and o.organizationId = ?2 and o.organizationLayoutMarkerId != ?3 " +
			"and o.organizationLayout.organizationLayoutId = ?4")		
})
public class OrganizationLayoutMarkers extends BaseEntity{
	
	
	public static final String GET_MARKER_DATA_FOR_LAYOUT = "getMarkerDataForLayout";
	public static final String GET_ACTIVE_MARKER_DATA_FOR_LAYOUT = "getActiveMarkerDataForLayout";
	public static final String GET_MARKER_DATA_FOR_DEFAULT_ORG_LAYOUT = "getMarkerDataForDefaultOrgLayout";
	
	public static final String GET_LAYOUT_MARKER_FROM_MARKERNAME= "fetchLayoutMarkerFromName";
	public static final String GET_LAYOUT_MARKER_FROM_MARKERNAME_WITHID= "fetchLayoutMarkerFromNameWithId";
	
	@Id
	@Column(name="OrganizationLayoutMarkerId")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long organizationLayoutMarkerId;
	
	@Column(name="OrganizationId")
	private Long organizationId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="OrganizationLayoutId")
	private OrganizationLayout organizationLayout;
	

	@Column(name="LayoutIdentificationName")
	private String layoutIdentificationName;
	
	@Column(name="LayoutMarkerCode")
	private String layoutMarkerCode;
	
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(columnDefinition = "TINYINT", nullable = false,name="LayoutMarkerQRCodeGenerated")
	private boolean layoutMarkerQRCodeGenerated;
	
	@Column(name="LayoutMarkerQRCodeImage")
	private  byte[]  layoutMarkerQRCodeImage;
	
	@Column(name="LayoutMarkerPosRowData")
	private Long layoutMarkerPosRowData;
	
	@Column(name="LayoutMarkerPosColData")
	private Long layoutMarkerPosColData;
	
	@Column(name="LayoutMarkerDataXSize")
	private Long layoutMarkerDataXSize;
	
	@Column(name="LayoutMarkerDataYSize")
	private Long layoutMarkerDataYSize;
	
	@Column(name="LayoutMarkerStyleClass")
	private String layoutMarkerStyleClass;
	
	@Column(name="LayoutMarkerStylePos")
	private String layoutMarkerStylePos;
	
	@Column(columnDefinition = "TINYINT",name="Active", nullable=false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean active;

	public Long getOrganizationLayoutMarkerId() {
		return organizationLayoutMarkerId;
	}

	public void setOrganizationLayoutMarkerId(Long organizationLayoutMarkerId) {
		this.organizationLayoutMarkerId = organizationLayoutMarkerId;
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

	public String getLayoutMarkerCode() {
		return layoutMarkerCode;
	}

	public void setLayoutMarkerCode(String layoutMarkerCode) {
		this.layoutMarkerCode = layoutMarkerCode;
	}

	public Long getLayoutMarkerPosRowData() {
		return layoutMarkerPosRowData;
	}

	public void setLayoutMarkerPosRowData(Long layoutMarkerPosRowData) {
		this.layoutMarkerPosRowData = layoutMarkerPosRowData;
	}

	public Long getLayoutMarkerPosColData() {
		return layoutMarkerPosColData;
	}

	public void setLayoutMarkerPosColData(Long layoutMarkerPosColData) {
		this.layoutMarkerPosColData = layoutMarkerPosColData;
	}

	public Long getLayoutMarkerDataXSize() {
		return layoutMarkerDataXSize;
	}

	public void setLayoutMarkerDataXSize(Long layoutMarkerDataXSize) {
		this.layoutMarkerDataXSize = layoutMarkerDataXSize;
	}

	public Long getLayoutMarkerDataYSize() {
		return layoutMarkerDataYSize;
	}

	public void setLayoutMarkerDataYSize(Long layoutMarkerDataYSize) {
		this.layoutMarkerDataYSize = layoutMarkerDataYSize;
	}

	public String getLayoutMarkerStyleClass() {
		return layoutMarkerStyleClass;
	}

	public void setLayoutMarkerStyleClass(String layoutMarkerStyleClass) {
		this.layoutMarkerStyleClass = layoutMarkerStyleClass;
	}

	public String getLayoutMarkerStylePos() {
		return layoutMarkerStylePos;
	}

	public void setLayoutMarkerStylePos(String layoutMarkerStylePos) {
		this.layoutMarkerStylePos = layoutMarkerStylePos;
	}

	public OrganizationLayout getOrganizationLayout() {
		return organizationLayout;
	}

	public void setOrganizationLayout(OrganizationLayout organizationLayout) {
		this.organizationLayout = organizationLayout;
	}

	public byte[] getLayoutMarkerQRCodeImage() {
		return layoutMarkerQRCodeImage;
	}

	public void setLayoutMarkerQRCodeImage(byte[] layoutMarkerQRCodeImage) {
		this.layoutMarkerQRCodeImage = layoutMarkerQRCodeImage;
	}

	public boolean isLayoutMarkerQRCodeGenerated() {
		return layoutMarkerQRCodeGenerated;
	}

	public void setLayoutMarkerQRCodeGenerated(boolean layoutMarkerQRCodeGenerated) {
		this.layoutMarkerQRCodeGenerated = layoutMarkerQRCodeGenerated;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.getOrganizationLayoutMarkerId().intValue() == ((OrganizationLayoutMarkers)obj).getOrganizationLayoutMarkerId().intValue();
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	
	

}
