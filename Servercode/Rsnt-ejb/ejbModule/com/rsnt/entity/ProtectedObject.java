package com.rsnt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.rsnt.common.entity.BaseEntity;

@Entity(name="ProtectedObject")
@Table(name="PROTECTEDOBJECT")
public class ProtectedObject extends BaseEntity{

	@Id
	@Column(name="ProtectedObjectID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long protectedObjectId;
	
	@Column(name="PROTECTEDOBJECTNAME")
	private String protectedObjectName;
	
	@Column(name="DESCRIPTION")
	private String description;

	public Long getProtectedObjectId() {
		return protectedObjectId;
	}

	public void setProtectedObjectId(Long protectedObjectId) {
		this.protectedObjectId = protectedObjectId;
	}

	public String getProtectedObjectName() {
		return protectedObjectName;
	}

	public void setProtectedObjectName(String protectedObjectName) {
		this.protectedObjectName = protectedObjectName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
