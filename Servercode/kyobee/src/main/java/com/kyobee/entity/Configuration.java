package com.kyobee.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
@Entity
@Table(name="CONFIGURATION")
public class Configuration implements java.io.Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	@Column(name="itemkey")
	private String itemkey;
	@Column(name="itemvalue")
    private String itemvalue;
    @Column(columnDefinition = "TINYINT",name="Active", nullable=false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean active;
    @Column(name="CreatedBy")
    private String createdBy;
    @Column(name="CreatedAt")
	@Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name="ModifiedBy")
    private String modifiedBy;
    @Column(name="ModifiedAt")
   	@Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;
    @Column(name="Description")
    private String description;
	public Configuration() {
		
	}

	public Configuration(Long id) {
	this.id = id;
	}

	
	
	public Configuration(Long id, String itemkey, String itemvalue, boolean active, String createdBy, Date createdAt,
			String modifiedBy, Date modifiedAt, String description) {
		super();
		this.id = id;
		this.itemkey = itemkey;
		this.itemvalue = itemvalue;
		this.active = active;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
		this.modifiedBy = modifiedBy;
		this.modifiedAt = modifiedAt;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getItemkey() {
		return itemkey;
	}

	public void setItemkey(String itemkey) {
		this.itemkey = itemkey;
	}

	public String getItemvalue() {
		return itemvalue;
	}

	public void setItemvalue(String itemvalue) {
		this.itemvalue = itemvalue;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Configuration [id=" + id + ", itemkey=" + itemkey + ", itemvalue=" + itemvalue + ", active=" + active
				+ ", createdBy=" + createdBy + ", createdAt=" + createdAt + ", modifiedBy=" + modifiedBy
				+ ", modifiedAt=" + modifiedAt + ", description=" + description + "]";
	}

	

	
    
    

}
