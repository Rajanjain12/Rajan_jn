package com.rsnt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.rsnt.common.entity.BaseEntity;

@Entity(name="RoleProtectedObject")
@Table(name="ROLEPROTECTEDOBJECT")
@NamedQueries({ @NamedQuery(
        name = RoleProtectedObject.FETCH_ROLE_PERMISSIONS_BY_USER_ID,
        query = "select po.protectedObjectName from RoleProtectedObject r, UserRole ur, ProtectedObject po where r.roleId = ur.roleId "
                + " and r.protectedObjectId=po.protectedObjectId and ur.user.userId = :userId") })
public class RoleProtectedObject  extends BaseEntity{

	 public static final String FETCH_ROLE_PERMISSIONS_BY_USER_ID = "fetchRolePermissionsByUserId";
	 
	@Id
	@Column(name="RoleProtectedObjectID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long roleProtectedObjectId;
	
	@Column(name="RoleID")
	private Long roleId;
	
	@Column(name="ProtectedObjectID")
	private Long protectedObjectId;
	
	
	/*@OneToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "ProtectedObjectID")
	private ProtectedObject protectedObject;*/
	

	public Long getRoleProtectedObjectId() {
		return roleProtectedObjectId;
	}

	public void setRoleProtectedObjectId(Long roleProtectedObjectId) {
		this.roleProtectedObjectId = roleProtectedObjectId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getProtectedObjectId() {
		return protectedObjectId;
	}

	public void setProtectedObjectId(Long protectedObjectId) {
		this.protectedObjectId = protectedObjectId;
	}


	
	
	
}
