package com.kyobee.entity;

import java.io.Serializable;

import org.hibernate.annotations.CascadeType;
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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;

@Entity
@Table(name="USER")
@NamedQueries({	
	//LogIn useing username or email id change done   by Aarshi(04/03/2019)
	@NamedQuery(name=User.GET_PROFILE_BY_USERLOGIN,query="Select u from User u join u.organizationUser ou join ou.organization o where lower(u.email)=:username or u.userName=:username and u.password =:password and o.clientBase =:clientBase"),
//	@NamedQuery(name=User.TEST_PROFILE_BY_USERLOGIN,query="Select u from User u join u.organizationUser ou join ou.organization o where lower(u.email)=:username or u.userName=:username and u.password =:password "),
	@NamedQuery(name=User.GET_PROFILE_BY_USERLOGINAPI,query="Select u from User u join u.organizationUser ou join ou.organization o where lower(u.email)=:username or u.userName=:username and u.password =:password"),
	@NamedQuery(name=User.GET_USER_ORGANIZATION,
			query="Select user.organizationUser.organization from User user join user.organizationUser where user.userId=:userId"),
			@NamedQuery(name=User.GET_USER_BY_USERNAME_ONLY,query="Select u from User u where lower(u.userName)=?1 "),
			@NamedQuery(name=User.GET_USER_BY_USERNAME,query="Select u from User u inner join u.organizationUser organizationUser " +
					"where lower(u.userName)=?1  and organizationUser.organization.organizationId = ?2 "),
			//pampaniya shweta for get user by email
			@NamedQuery(name=User.GET_USER_BY_EMAIL,query="Select u from User u join fetch u.organizationUser ou join fetch ou.organization o where lower(u.email)=:email and u.active=:active and o.activeFlag=:oactive")
				})
public class User extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 4651563203184196025L;
	public static final String GET_PROFILE_BY_USERLOGIN = "getProfileByUserLogin";
	public static final String GET_PROFILE_BY_USERLOGINAPI="getProfileByUserLoginAPI";
	public static final String GET_USER_ORGANIZATION = "getUserOrganization";
	public static final String GET_USER_BY_USERNAME = "getUserByUserName";
	public static final String GET_USER_BY_USERNAME_ONLY = "getUserByUserNameOnly";
	public static final String GET_USER_BY_EMAIL= "getUserByEmail";
	//public static final String TEST_PROFILE_BY_USERLOGIN="getUserByEmail";
	
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
	
	@OneToOne(mappedBy="user", fetch = FetchType.LAZY)
	@Cascade(CascadeType.ALL)
	@JoinColumn(name = "UserID", referencedColumnName="UserID")
	private OrganizationUser organizationUser;

	@OneToOne(mappedBy="user", fetch = FetchType.LAZY)
	@Cascade(CascadeType.ALL)
	@JoinColumn(name="UserID", referencedColumnName="UserID")
	private UserRole userRole;
	
	@Column(name="ActivationID")
	private String activationId;
	
	//Pampaniya Shweta for Add AuthCode for Sending Email for Forgot Password
	@Column(name="AuthCode")
	private String authcode;
	

	//Aarshi
	@OneToOne(fetch = FetchType.LAZY, cascade= {javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.REMOVE})
	@JoinColumn(name="AddressId")
	private Address address1;
	
	public Address getAddress1() {
		return address1;
	}

	public void setAddress1(Address address1) {
		this.address1 = address1;
	}

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

	@Cascade(CascadeType.SAVE_UPDATE)
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
	
	//Pampaniya Shweta For ForgotPassword
	public String getAuthcode() {
		return authcode;
	}

	public String setAuthcode(String authcode) {
		return this.authcode = authcode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
