package com.rsnt.util.transferobject;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.jboss.seam.annotations.Name;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Name("layoutTableDataTO")
public class LayoutTableInfoTO implements Serializable {

	@JsonProperty
	private String organizationLayoutMarkerId;
	
	@JsonProperty
	private String tableName;
	
	@JsonProperty
	private String qrCodeGenerated;
	
	@JsonProperty
	private String activeStatus;

	@JsonProperty
	private Long access;	

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getQrCodeGenerated() {
		return qrCodeGenerated;
	}

	public void setQrCodeGenerated(String qrCodeGenerated) {
		this.qrCodeGenerated = qrCodeGenerated;
	}

	public String getOrganizationLayoutMarkerId() {
		return organizationLayoutMarkerId;
	}

	public void setOrganizationLayoutMarkerId(String organizationLayoutMarkerId) {
		this.organizationLayoutMarkerId = organizationLayoutMarkerId;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Long getAccess() {
		return access;
	}

	public void setAccess(Long access) {
		this.access = access;
	}

	
	
}
