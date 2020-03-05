package com.kyobeeUserService.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name="USERROLE")
public class Userrole implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="UserRoleID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer userRoleID;

	@Temporal(TemporalType.DATE)
	@Column(name="CreatedAt")
	private Date createdAt;

	@Column(name="CreatedBy")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="ModifiedAt")
	private Date modifiedAt;

	@Column(name="ModifiedBy")
	private String modifiedBy;

	//bi-directional many-to-one association to User
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="UserID")
	private User user;

	//bi-directional many-to-one association to Lookup
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="RoleID")
	private Role role;

	//bi-directional many-to-one association to User
	/*
	 * @ManyToOne
	 * 
	 * @JoinColumn(name="UserID") private User user2;
	 */

	public Userrole() {
	}

	public Integer getUserRoleID() {
		return this.userRoleID;
	}

	public void setUserRoleID(Integer userRoleID) {
		this.userRoleID = userRoleID;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedAt() {
		return this.modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
    
}
