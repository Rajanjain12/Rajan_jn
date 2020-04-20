package com.kyobeeUserService.dto;

public class OrganizationTypeDTO {

	private Integer typeID;
	private String typeName;

	public OrganizationTypeDTO(Integer typeID, String typeName) {
		this.typeID = typeID;
		this.typeName = typeName;
	}

	public Integer getTypeID() {
		return typeID;
	}

	public void setTypeID(Integer typeID) {
		this.typeID = typeID;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
