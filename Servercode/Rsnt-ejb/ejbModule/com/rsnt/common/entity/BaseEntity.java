package com.rsnt.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public class BaseEntity implements Serializable {

	@Column(name="CreatedBy")
	private String createdBy;
	
	@Column(name="CreatedAt")
	private Date createdDate;
	
	@Column(name="ModifiedBy")
	private String modifiedBy;
	
	@Column(name="ModifiedAt")
	private Date modifiedDate;

	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	@PrePersist
    protected void onCreate() {
      createdDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
    	modifiedDate = new Date();
    }
}
