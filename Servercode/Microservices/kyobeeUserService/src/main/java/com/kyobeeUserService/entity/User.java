package com.kyobeeUserService.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;

@Entity
@Table(name = "USER")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "UserID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userID;

	@Column(name = "UserName")
	private String userName;

	@Column(name = "Password")
	private String password;
	
	@Column(name = "SaltString")
	private String saltString;

	@Column(name = "FirstName")
	private String firstName;

	@Column(name = "LastName")
	private String lastName;

	@Column(name = "ContactNoOne")
	private String contactNoOne;

	@Column(name = "ContactNoTwo")
	private String contactNoTwo;

	@Column(name = "Email")
	private String email;

	// bi-directional many-to-one association to Address
	@ManyToOne
	@JoinColumn(name = "AddressId")
	private Address addressBean;

	@Column(name = "AuthCode")
	private String authCode;

	@Column(name = "ActivationCode")
	private String activationCode;

	@Column(name = "ActivationExpiryDate")
	private Date activationExpiryDate;

	@Column(name = "Active")
	private Byte active;

	@Temporal(TemporalType.DATE)
	@Column(name = "CreatedAt")
	private Date createdAt;

	@Column(name = "CreatedBy")
	private String createdBy;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "ModifiedAt")
	private Date modifiedAt;

	// bi-directional many-to-one association to Organizationuser
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<OrganizationUser> organizationusers;

	
	public User() {
	}

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSaltString() {
		return saltString;
	}

	public void setSaltString(String saltString) {
		this.saltString = saltString;
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

	public String getContactNoOne() {
		return contactNoOne;
	}

	public void setContactNoOne(String contactNoOne) {
		this.contactNoOne = contactNoOne;
	}

	public String getContactNoTwo() {
		return contactNoTwo;
	}

	public void setContactNoTwo(String contactNoTwo) {
		this.contactNoTwo = contactNoTwo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Address getAddressBean() {
		return addressBean;
	}

	public void setAddressBean(Address addressBean) {
		this.addressBean = addressBean;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	public Date getActivationExpiryDate() {
		return activationExpiryDate;
	}

	public void setActivationExpiryDate(Date activationExpiryDate) {
		this.activationExpiryDate = activationExpiryDate;
	}

	public Byte getActive() {
		return active;
	}

	public void setActive(Byte active) {
		this.active = active;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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

	public List<OrganizationUser> getOrganizationusers() {
		return organizationusers;
	}

	public void setOrganizationusers(List<OrganizationUser> organizationusers) {
		this.organizationusers = organizationusers;
	}

}
