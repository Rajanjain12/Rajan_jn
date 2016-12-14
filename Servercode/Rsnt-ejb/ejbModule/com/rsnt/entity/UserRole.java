package com.rsnt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.rsnt.common.entity.BaseEntity;

@Entity(name="UserRole")
@Table(name="USERROLE")
public class UserRole extends BaseEntity{

	@Id
	@Column(name="UserRoleId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userRoleId;
	

	@OneToOne
	@JoinColumn(name="UserID")
	private User user;
	
	@Column(name="RoleID")
	private Long roleId;

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	
	
	
}
