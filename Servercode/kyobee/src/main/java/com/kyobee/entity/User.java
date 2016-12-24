package com.kyobee.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="USER")
@NamedQueries({@NamedQuery(name=User.GET_PROFILE_BY_USERLOGIN,query="Select u from User u where lower(u.userName)=:username and active is true"),
	@NamedQuery(name=User.GET_USER_ORGANIZATION,
			query="Select user.organizationUser.organization from User user join user.organizationUser where user.userId=?1"),
			@NamedQuery(name=User.GET_USER_BY_USERNAME_ONLY,query="Select u from User u where lower(u.userName)=?1 "),
			@NamedQuery(name=User.GET_USER_BY_USERNAME,query="Select u from User u inner join u.organizationUser organizationUser " +
					"where lower(u.userName)=?1  and organizationUser.organization.organizationId = ?2 "),
			@NamedQuery(name=User.GET_USER_BY_EMAIL,query="Select u from User u where lower(u.email)=?1 and active is true")
				})
public class User extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 4651563203184196025L;
	public static final String GET_PROFILE_BY_USERLOGIN = "getProfileByUserLogin";
	public static final String GET_USER_ORGANIZATION = "getUserOrganization";
	public static final String GET_USER_BY_USERNAME = "getUserByUserName";
	public static final String GET_USER_BY_USERNAME_ONLY = "getUserByUserNameOnly";
	public static final String GET_USER_BY_EMAIL= "getUserByEmail";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="UserId")
	private Long userId;
	
	@Column(name="UserName")
	private String userName;
	
	@Column(name="Password")
	private String password;
	
	@Column(name="FirstName")
	private String firstName;
	
	@Column(name="LastName")
	private String lastName;
	
	@Column(name="Address")
	private String  address;
	
	@Column(name="ContactNoOne")
	private String primaryContactNo;
	
	@Column(name="ContactNoTwo")
	private String alternateContactNo;
	
	@Column(name="Email")
	private String email;
	
	@Column(columnDefinition = "TINYINT",name="Active", nullable=false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean active;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UserID", referencedColumnName="UserId")
	private OrganizationUser organizationUser;

	@OneToOne(mappedBy="user", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name="UserID", referencedColumnName="UserID")
	private UserRole userRole;
	
	@Column(name="ActivationID")
	private String activationId;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPrimaryContactNo() {
		return primaryContactNo;
	}

	public void setPrimaryContactNo(String primaryContactNo) {
		this.primaryContactNo = primaryContactNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAlternateContactNo() {
		return alternateContactNo;
	}

	public void setAlternateContactNo(String alternateContactNo) {
		this.alternateContactNo = alternateContactNo;
	}

	public OrganizationUser getOrganizationUser() {
		return organizationUser;
	}

	public void setOrganizationUser(OrganizationUser organizationUser) {
		this.organizationUser = organizationUser;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public String getActivationId() {
		return activationId;
	}

	public void setActivationId(String activationId) {
		this.activationId = activationId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}
